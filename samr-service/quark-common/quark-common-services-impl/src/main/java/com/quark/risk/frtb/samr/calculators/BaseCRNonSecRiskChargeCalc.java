package com.quark.risk.frtb.samr.calculators;

import com.quark.risk.frtb.samr.model.CreditIssuerInfo;
import com.quark.risk.frtb.samr.model.RiskClassAndSensitivity;
import com.quark.risk.frtb.samr.model.TradeSensitivity;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseCRNonSecRiskChargeCalc extends BaseRiskChargeCalculator {

    protected List<CreditSens> sensitivities = new ArrayList<>();

    @Override
    protected void filter() {
        for (TradeSensitivity sens : calcRequest.getTradeSensitivities()) {
            if (getRiskClassAndSensitivity() == sens.getSensitivityType()) {
                CreditSens d = getCreditSensFromTradeSensitivity(sens);
                sensitivities.add(d);
            }
        }
    }

    protected Map<Integer, List<Group>> groupByBuckets(Map<GroupingKey, Group> bucketDeltas) {
        Map<Integer, List<Group>> buckets = new TreeMap<>();
        for (Map.Entry<GroupingKey, Group> me : bucketDeltas.entrySet()) {
            if (!buckets.containsKey(me.getKey().getBucket())) {
                buckets.put(me.getKey().getBucket(), new ArrayList<>());
            }
            buckets.get(me.getKey().getBucket()).add(me.getValue());
            log.debug(me.getValue().toString());
        }
        return buckets;
    }

    protected Map<GroupingKey, Group> applyFxRwAndSensFactor(Map<CurrGroupingKey, CurrGrouping> currencyDeltas) {
        Map<GroupingKey, Group> aggrDeltas = new TreeMap<>();
        for (Map.Entry<CurrGroupingKey, CurrGrouping> me : currencyDeltas.entrySet()) {
            GroupingKey key = new GroupingKey(me.getKey().getBucket(), me.getKey().getIssuerId(),
                    me.getKey().getBondOrCds());
            double factor = getRate(me.getKey().getCurrency()) * getRw(me.getKey().getBucket()) * getSensFactor();
            if (!aggrDeltas.containsKey(key)) {
                Group g = getGroupFromCurrGrouping(me.getValue());
                g.scale(factor);
                aggrDeltas.put(key, g);
            } else {
                aggrDeltas.get(key).addApplyingFactor(factor, me.getValue().getDeltaMap());
            }
        }
        return aggrDeltas;
    }

    protected Map<CurrGroupingKey, CurrGrouping> groupByCurrency() {
        Map<CurrGroupingKey, CurrGrouping> currencyDeltas = new TreeMap<>();
        for (CreditSens d : sensitivities) {
            CurrGroupingKey key = new CurrGroupingKey(d.getCurrency(), d.getBucket(), d.getIssuerId(),
                    d.getBondOrCds());
            if (!currencyDeltas.containsKey(key)) {
                currencyDeltas.put(key, new CurrGrouping(key));
            }
            currencyDeltas.get(key).add(new TenorDelta(d.getTenor(), d.getTenorCode(), d.getAmount()));
        }
        return currencyDeltas;
    }

    protected boolean assignIssuerInfo() {
        Map<String, CreditIssuerInfo> infoMap = getTradeIdCreditIssuerMap();
        boolean anyMappingDone = false;
        for (CreditSens d : sensitivities) {
            CreditIssuerInfo i = infoMap.get(d.getIdentifier());
            if (i == null) {
                messages.add(String.format("No credit issuer info for trade : %s", d.getIdentifier()));
                continue;
            }
            d.setBucket(i.getBucket());
            d.setIssuerId(i.getIssuer());
            d.setBondOrCds(i.getBondOrCds());
            d.setRating(i.getRating());
            d.setSector(i.getSector());
            anyMappingDone = true;
        }
        return anyMappingDone;
    }

    protected Map<String, CreditIssuerInfo> getTradeIdCreditIssuerMap() {
        return calcRequest.getCreditIssuerInfo().stream()
                .collect(Collectors.toMap(CreditIssuerInfo::getTradeIdentifier, Function.identity()));
    }

    protected RealMatrix getDeltaVector(List<Group> groups) {
        int[] tenors = getTenorCodes();
        int dim = groups.size() * tenors.length;
        RealMatrix v = new Array2DRowRealMatrix(dim, 1);
        int i = 0;
        for (Group g : groups) {
            for (Integer tnr : tenors) {
                TenorDelta delta = g.getDeltaMap().get(tnr);
                v.setEntry(i++, 0, delta != null ? delta.getAmount() : 0);
            }
        }
        return v;
    }

    protected abstract double getSensFactor();

    protected abstract int[] getTenorCodes();

    protected abstract RiskClassAndSensitivity getRiskClassAndSensitivity();

    protected abstract double getRw(int bucket);

    @Setter
    @Getter
    @ToString
    protected class BucketLevelResults {
        private int bucket;
        private double kb;
        private double sb;
        private double sb2;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    protected class Group {
        private GroupingKey key;
        private Map<Integer, TenorDelta> deltaMap = new HashMap<>();

        Group(GroupingKey key) {
            this.key = key;
        }

        public void scale(double rate) {
            for (Map.Entry<Integer, TenorDelta> me : deltaMap.entrySet()) {
                me.getValue().scale(rate);
            }
        }

        public void addApplyingFactor(double rate, Map<Integer, TenorDelta> m) {
            for (Map.Entry<Integer, TenorDelta> me : deltaMap.entrySet()) {
                TenorDelta d = m.get(me.getValue().getTenorCode());
                d.scale(rate);
                me.getValue().add(d);
            }
        }
    }

    protected Group getGroupFromCurrGrouping(CurrGrouping value) {
        Group g = new Group(new GroupingKey(value.getKey().getBucket(), value.getKey().getIssuerId(),
                value.getKey().getBondOrCds()));
        Map<Integer, TenorDelta> m = new HashMap<>();
        for (Map.Entry<Integer, TenorDelta> me : value.getDeltaMap().entrySet()) {
            m.put(me.getKey(), (TenorDelta) me.getValue().clone());
        }
        g.setDeltaMap(m);
        return g;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    protected class GroupingKey implements Comparable<GroupingKey> {
        protected int bucket;
        protected String issuerId;
        protected String bondOrCds;

        @Override
        public int compareTo(GroupingKey o) {
            return new CompareToBuilder().append(this.bucket, o.bucket).append(this.issuerId, o.issuerId)
                    .append(this.bondOrCds, o.bondOrCds).toComparison();
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    protected class CurrGrouping {
        private CurrGroupingKey key;
        private Map<Integer, TenorDelta> deltaMap = new HashMap<>();

        CurrGrouping(CurrGroupingKey key) {
            this.key = key;
        }

        void add(TenorDelta d) {
            if (!deltaMap.containsKey(d.getTenorCode())) {
                deltaMap.put(d.getTenorCode(), d);
            } else {
                deltaMap.get(d.getTenorCode()).add(d);
            }
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    protected class CurrGroupingKey implements Comparable<CurrGroupingKey> {
        private String currency;
        protected int bucket;
        protected String issuerId;
        protected String bondOrCds;

        public int compareTo(CurrGroupingKey o) {
            return new CompareToBuilder().append(this.currency, o.currency).append(this.bucket, o.bucket)
                    .append(this.issuerId, o.issuerId).append(this.bondOrCds, o.bondOrCds).toComparison();
        }
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @ToString
    protected class TenorDelta implements Cloneable {
        private String tenor;
        private int tenorCode;
        private double amount;

        void add(TenorDelta d) {
            if (d != null && !Double.isNaN(d.getAmount())) {
                this.amount += d.getAmount();
            }
        }

        public void scale(double rate) {
            amount *= rate;
        }

        @Override
        protected Object clone() {
            return new TenorDelta(tenor, tenorCode, amount);
        }
    }

    @Setter
    @Getter
    @ToString
    protected class CreditSens {
        private String tenor;
        private int tenorCode;
        private double amount;
        private String identifier;
        private String currency;
        private Integer bucket;
        private String issuerId;
        private String issuer;
        private String bondOrCds;
        private String rating;
        private String sector;

        CreditSens(String identifier, String currency, String tenor, int tenorCode, double amount) {
            this.identifier = identifier;
            this.currency = currency;
            this.tenor = tenor;
            this.tenorCode = tenorCode;
            this.amount = amount;
        }
    }

    protected CreditSens getCreditSensFromTradeSensitivity(TradeSensitivity tradeSens) {
        return new CreditSens(tradeSens.getTradeIdentifier(), tradeSens.getCurrency(), tradeSens.getTenor(),
                tradeSens.getTenorCode(), tradeSens.getSensitivity());
    }
}

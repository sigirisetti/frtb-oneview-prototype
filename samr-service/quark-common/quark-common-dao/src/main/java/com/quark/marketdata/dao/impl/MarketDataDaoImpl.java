package com.quark.marketdata.dao.impl;

import com.quark.marketdata.curve.Curve;
import com.quark.marketdata.curve.CurveUnderlying;
import com.quark.marketdata.curve.CurveUnderlyingMap;
import com.quark.marketdata.dao.MarketDataDao;
import com.quark.marketdata.quote.Quote;
import com.quark.marketdata.quote.QuoteSet;
import com.quark.marketdata.search.QuoteSearchInput;
import com.quark.pricing.config.MarketDataSet;
import com.quark.pricing.config.PricingContextConfig;
import com.quark.pricing.config.PricingEngineConfig;

import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate5.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Repository("MarketDataDao")
public class MarketDataDaoImpl extends HibernateDaoSupport implements MarketDataDao {

	private static final String KEY_TIMESTAMP = "key.timestamp";
	private static final String KEY_QUOTE_NAME = "key.quoteName";
	private static final String KEY_QUOTE_SET_NAME = "key.quoteSetName";

	@Autowired
	public MarketDataDaoImpl(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Override
	public List<String> getAllQuoteSetNames() {
		Session session = getSessionFactory().getCurrentSession();
		return session.createQuery("select quoteSetName from QuoteSet").setCacheable(true).list();
	}

	@Override
	public void save(QuoteSet qs) {
		Session session = getSessionFactory().getCurrentSession();
		session.save(qs);
		for (Quote q : qs.getQuotes()) {
			session.save(q.getQuoteName());
			session.save(q);
		}
	}

	@Override
	public Map<String, List<String>> getAllQuoteNames() {
		Session session = getSessionFactory().getCurrentSession();
		@SuppressWarnings("unchecked")
		List<Object[]> names = session
				.createQuery("select distinct quoteName.quoteName, quoteSet.quoteSetName from Quote").setCacheable(true)
				.list();
		Map<String, List<String>> m = new HashMap<>();
		for (Object[] o : names) {
			List<String> v = m.get(o[1]);
			if (v == null) {
				v = new ArrayList<>();
				m.put((String) o[1], v);
			}
			v.add((String) o[0]);
		}
		return m;
	}

	@Override
	public List<Quote> searchQuotes(QuoteSearchInput input) {
		String qn = input.getQuoteName();
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Quote.class);
		c.add(Restrictions.eq(KEY_QUOTE_SET_NAME, input.getQuoteSet()));
		if (input.isCaseSensitive()) {
			if (input.isExactMatch()) {
				c.add(Restrictions.eq(KEY_QUOTE_NAME, qn));
			} else {
				c.add(Restrictions.like(KEY_QUOTE_NAME, qn));
			}
		} else {
			if (input.isExactMatch()) {
				c.add(Restrictions.eq(KEY_QUOTE_NAME, qn).ignoreCase());
			} else {
				c.add(Restrictions.ilike(KEY_QUOTE_NAME, qn, MatchMode.ANYWHERE));
			}
		}
		if (input.getFrom() != null && input.getTo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(input.getTo());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.SECOND, -1);
			c.add(Restrictions.between(KEY_TIMESTAMP, input.getFrom(), cal.getTime()));
		} else if (input.getFrom() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(input.getFrom());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.SECOND, -1);
			c.add(Restrictions.between(KEY_TIMESTAMP, input.getFrom(), cal.getTime()));
		} else if (input.getTo() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(input.getTo());
			cal.add(Calendar.DAY_OF_MONTH, 1);
			cal.add(Calendar.SECOND, -1);
			c.add(Restrictions.between(KEY_TIMESTAMP, input.getTo(), cal.getTime()));
		}

		List quotes = c.setCacheable(true).list();
		return quotes;
	}

	@Override
	public QuoteSet getQuoteSet(Timestamp ts, String qsName) {
		Session session = getSessionFactory().getCurrentSession();
		QuoteSet qs = session.load(QuoteSet.class, qsName);
		qs.setQuotes(new HashSet<Quote>(loadQuotes(ts, qsName)));
		return qs;
	}

	@Override
	public QuoteSet getQuoteSet(String qsName) {
		Session session = getSessionFactory().getCurrentSession();
		return session.load(QuoteSet.class, qsName);
	}

	@Override
	public void saveQuotes(List<Quote> quotes) {
		Session session = getSessionFactory().getCurrentSession();
		for (Quote q : quotes) {
			session.saveOrUpdate(q);
		}
	}

	@Override
	public void saveQuotes(String pCtx, List<Quote> quotes) {
		Session session = getSessionFactory().getCurrentSession();
		String pcQSName = getPricingCtxQuoteSetName(pCtx);
		for (Quote q : quotes) {
			q.getKey().setQuoteSetName(pcQSName);
			session.saveOrUpdate(q.getQuoteName());
			session.saveOrUpdate(q);
		}
	}

	@Override
	public CurveUnderlying save(CurveUnderlying cu) {
		Session session = getSessionFactory().getCurrentSession();
		if (cu.getId() != null) {
			session.update(cu);
		} else {
			session.save(cu);
		}
		return cu;
	}

	@Override
	public void delete(CurveUnderlying... cu) {
		Session session = getSessionFactory().getCurrentSession();
		for (CurveUnderlying d : cu) {
			session.refresh(d);
			session.delete(d);
		}
	}

	@Override
	public List<CurveUnderlying> getCurveUnderlyings(Class type) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(type);
		List<CurveUnderlying> list = c.setCacheable(true).list();
		return list;
	}

	@Override
	public List<CurveUnderlying> getAllCurveUnderlyings() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(CurveUnderlying.class);
		List<CurveUnderlying> list = c.setCacheable(true).list();
		return list;
	}

	@Override
	public void saveCurveUnderlyings(List<CurveUnderlying> list) {
		Session session = getSessionFactory().getCurrentSession();
		for (CurveUnderlying u : list) {
			session.saveOrUpdate(u);
		}
	}

	@Override
	public List<Curve> getAllCurves() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Curve.class);
		c.addOrder(Order.asc("currency"));
		c.addOrder(Order.asc("name"));
		c.addOrder(Order.asc("instance"));
		return c.setCacheable(true).list();
	}

	@Override
	public List<String> getPricingCtxNames() {
		List<String> names = new ArrayList<>();
		names.add(PricingContextConfig.DEFAULT_PRICING_CONTEXT_CONFIG);
		return names;
	}

	@Override
	public String getPricingCtxQuoteSetName(String pCtxName) {
		return PricingContextConfig.DEFAULT_QUOTE_SET_NAME;
	}

	@Override
	public List<Quote> getQuotes(Date date, String pCtx, List<Quote> qList) {
		List<Quote> refreshedQuotes = new ArrayList<>();
		PricingContextConfig pcc = getPricingContextConfig(pCtx);
		QuoteSet qs = pcc.getQuoteSet();
		qs.setQuotes(
				new HashSet<Quote>(loadAllQuotesForGivenDate(new java.sql.Date(date.getTime()), qs.getQuoteSetName())));
		for (Quote q : qList) {
			Quote refreshedQuote = qs.getLatestQuote(q.getQuoteName().getQuoteName());
			if (refreshedQuote != null) {
				refreshedQuotes.add(refreshedQuote);
			} else {
				refreshedQuotes.add(q);
			}
		}
		return refreshedQuotes;
	}

	@Override
	public List<Quote> loadQuotes(Timestamp ts, String qsName) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Quote.class);
		c.add(Restrictions.eq(KEY_QUOTE_SET_NAME, qsName));
		c.add(Restrictions.between(KEY_TIMESTAMP, new java.sql.Date(ts.getTime()), ts));
		return c.setCacheable(true).list();
	}

	@Override
	public List<Quote> loadAllQuotesForGivenDate(java.sql.Date date, String qsName) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.SECOND, -1);
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Quote.class);
		c.add(Restrictions.eq(KEY_QUOTE_SET_NAME, qsName));
		c.add(Restrictions.between(KEY_TIMESTAMP, date, cal.getTime()));
		return c.setCacheable(true).list();
	}

	@Override
	public Curve save(Curve curve) {
		Session session = getSessionFactory().getCurrentSession();
		if (curve.getId() == null) {
			session.save(curve);
			for (CurveUnderlyingMap m : curve.getUnderlyingMap()) {
				m.setCurveId(curve.getId());
				session.save(m);
			}
		} else {
			session.update(curve);
			clearUnderlyingMapping(curve.getId());
			for (CurveUnderlyingMap m : curve.getUnderlyingMap()) {
				m.setCurveId(curve.getId());
				session.save(m);
			}
		}
		return curve;
	}

	public void clearUnderlyingMapping(Long curveId) {
		Session session = getSessionFactory().getCurrentSession();
		session.createQuery("delete from CurveUnderlyingMap where curveId = :curveId").setLong("curveId", curveId)
				.executeUpdate();
	}

	@Override
	public Curve getCurve(Long pk) {
		Session session = getSessionFactory().getCurrentSession();
		Curve curve = session.load(Curve.class, pk);
		Criteria c = session.createCriteria(CurveUnderlyingMap.class);
		c.add(Restrictions.eq("curveId", pk));
		curve.setUnderlyingMap(c.setCacheable(true).list());
		return curve;
	}

	@Override
	public Curve getCurve(String currency, String rateIndex, String rateIndexTenor) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(Curve.class);
		c.add(Restrictions.eq("currency", currency));
		c.add(Restrictions.eq("rateIndex", rateIndex));
		c.add(Restrictions.eq("rateIndexTenor", rateIndexTenor));
		List l = c.setCacheable(true).list();
		if (l.size() == 1) {
			return (Curve) l.get(0);
		} else {
			return null;
		}
	}

	@Override
	public Boolean deleteCurve(Long id) {
		Session session = getSessionFactory().getCurrentSession();
		clearUnderlyingMapping(id);
		Curve curve = session.load(Curve.class, id);
		session.delete(curve);
		return Boolean.TRUE;
	}

	@Override
	public PricingEngineConfig save(PricingEngineConfig config) {
		Session session = getSessionFactory().getCurrentSession();
		if (config.getId() == null) {
			session.save(config);
		} else {
			session.update(config);
		}
		return config;
	}

	@Override
	public List<String> getPricingEngineConfigNames() {
		Session session = getSessionFactory().getCurrentSession();
		Query query = session.createQuery("select pe.pricingEngineConfigName from PricingEngineConfig pe");
		return (List<String>) query.list();
	}

	@Override
	public PricingEngineConfig getPricingEngineConfig(String peConfig) {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(PricingEngineConfig.class);
		c.add(Restrictions.eq("pricingEngineConfigName", peConfig));
		List<?> l = c.list();
		if (l == null || l.isEmpty()) {
			return null;
		}
		return (PricingEngineConfig) c.list().get(0);
	}

	@Override
	public Boolean delete(PricingEngineConfig peConfig) {
		Session session = getSessionFactory().getCurrentSession();
		PricingEngineConfig d = session.load(PricingEngineConfig.class, peConfig.getId());
		session.delete(d);
		return Boolean.TRUE;
	}

	@Override
	public PricingContextConfig getPricingContextConfig(Timestamp ts, String name) {
		PricingContextConfig pc = getPricingContextConfig(name);
		pc.getQuoteSet().setQuotes(new HashSet<Quote>(loadQuotes(ts, pc.getQuoteSet().getQuoteSetName())));
		return pc;
	}

	@Override
	public PricingContextConfig save(PricingContextConfig pc) {
		Session session = getSessionFactory().getCurrentSession();
		PricingEngineConfig pec = getPricingEngineConfig(pc.getPricingEngineConfig().getPricingEngineConfigName());
		Hibernate.initialize(pec);
		pc.setPricingEngineConfig(pec);
		QuoteSet qs = getQuoteSet(pc.getQuoteSet().getQuoteSetName());
		Hibernate.initialize(qs);
		pc.setQuoteSet(qs);
		session.save(pc);
		return pc;
	}

	@Override
	public PricingContextConfig getPricingContextConfig(String name) {
		Session session = getSessionFactory().getCurrentSession();
		PricingContextConfig pc = session.load(PricingContextConfig.class, name);
		Hibernate.initialize(pc);
		return pc;
	}

	@Override
	public List<PricingContextConfig> getPricingContextConfigs() {
		Session session = getSessionFactory().getCurrentSession();
		Criteria c = session.createCriteria(PricingContextConfig.class);
		return c.list();
	}

	@Override
	public Boolean delete(PricingContextConfig pc) {
		Session session = getSessionFactory().getCurrentSession();
		session.delete(pc);
		return Boolean.TRUE;
	}

	@Override
	public List<CurveUnderlying> getCurveUnderlyings(List<CurveUnderlyingMap> underlyingMap) {
		Set<Long> underlyingIds = new TreeSet<>();
		for (CurveUnderlyingMap cuMap : underlyingMap) {
			underlyingIds.add(cuMap.getUnderlyingId());
		}
		return getCurveUnderlyings(underlyingIds);
	}

	@Override
	public List<CurveUnderlying> getCurveUnderlyings(Collection<Long> underlyingIds) {
		List<CurveUnderlying> curveUnderlyings = new ArrayList<>();
		List<CurveUnderlying> all = getAllCurveUnderlyings();
		for (CurveUnderlying cu : all) {
			if (underlyingIds.contains(cu.getId())) {
				curveUnderlyings.add(cu);
			}
		}
		return curveUnderlyings;
	}

	@Override
	public MarketDataSet save(MarketDataSet config) {
		Session session = getSessionFactory().getCurrentSession();
		if (config.getId() == null) {
			session.save(config);
		} else {
			session.update(config);
		}
		return config;
	}
}

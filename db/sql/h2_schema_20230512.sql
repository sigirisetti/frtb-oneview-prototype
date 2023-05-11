
    drop table if exists curve_def CASCADE ;

    drop table if exists curve_underlying_fra CASCADE ;

    drop table if exists curve_underlying_future CASCADE ;

    drop table if exists curve_underlying_map CASCADE ;

    drop table if exists curve_underlying_money_market CASCADE ;

    drop table if exists curve_underlying_swap CASCADE ;

    drop table if exists frtb_samr_commodity_info CASCADE ;

    drop table if exists frtb_samr_credit_issuer_info CASCADE ;

    drop table if exists frtb_samr_drc_non_sec CASCADE ;

    drop table if exists frtb_samr_drc_sec_ctp CASCADE ;

    drop table if exists frtb_samr_drc_sec_non_ctp CASCADE ;

    drop table if exists frtb_samr_eq_classification CASCADE ;

    drop table if exists frtb_samr_equity_info CASCADE ;

    drop table if exists frtb_samr_h_result_rows CASCADE ;

    drop table if exists frtb_samr_hierarchy CASCADE ;

    drop table if exists frtb_samr_inter_res CASCADE ;

    drop table if exists frtb_samr_market_quote CASCADE ;

    drop table if exists frtb_samr_po_results CASCADE ;

    drop table if exists frtb_samr_residuals CASCADE ;

    drop table if exists frtb_samr_results CASCADE ;

    drop table if exists frtb_samr_risk_class_results CASCADE ;

    drop table if exists frtb_samr_trade_sensitivity CASCADE ;

    drop table if exists frtb_samr_validation_messages CASCADE ;

    drop table if exists market_data_set CASCADE ;

    drop table if exists mdi_discount_curve CASCADE ;

    drop table if exists mdi_forward_curve CASCADE ;

    drop table if exists name_value_ref_data CASCADE ;

    drop table if exists pricing_context_config CASCADE ;

    drop table if exists pricing_engine_config CASCADE ;

    drop table if exists pricing_engine_config_item CASCADE ;

    drop table if exists q_exec_info CASCADE ;

    drop table if exists q_organization CASCADE ;

    drop table if exists q_permissions CASCADE ;

    drop table if exists q_scheduled_task_config CASCADE ;

    drop table if exists q_scheduled_task_properties CASCADE ;

    drop table if exists q_user CASCADE ;

    drop table if exists q_user_group CASCADE ;

    drop table if exists q_user_group_map CASCADE ;

    drop table if exists q_workflow_config CASCADE ;

    drop table if exists q_workflow_instance CASCADE ;

    drop table if exists q_workflow_instance_properties CASCADE ;

    drop table if exists quote CASCADE ;

    drop table if exists quote_name CASCADE ;

    drop table if exists quote_set CASCADE ;

    drop table if exists saccr_collateral CASCADE ;

    drop table if exists saccr_market_quote CASCADE ;

    drop table if exists saccr_req_trade_attrs CASCADE ;

    drop table if exists saccr_supervisory_parameters CASCADE ;

    drop table if exists saccr_trade CASCADE ;

    drop table if exists saccr_validation_messages CASCADE ;

    drop sequence if exists cu_id_seq;

    drop sequence if exists curve_def_id_seq;

    drop sequence if exists exec_info_id_seq;

    drop sequence if exists market_data_set_id_seq;

    drop sequence if exists mdi_id_seq;

    drop sequence if exists pe_config_id_seq;

    drop sequence if exists pe_config_item_id_seq;

    drop sequence if exists q_group_id_seq;

    drop sequence if exists q_permissions_id_seq;

    drop sequence if exists q_sch_task_prop_id_seq;

    drop sequence if exists q_scheduled_task_config_id_seq;

    drop sequence if exists q_user_group_id_seq;

    drop sequence if exists q_user_id_seq;

    drop sequence if exists q_workflow_config_id_seq;
create sequence cu_id_seq start with 1 increment by 50;
create sequence curve_def_id_seq start with 1 increment by 50;
create sequence exec_info_id_seq start with 1 increment by 50;
create sequence market_data_set_id_seq start with 1 increment by 50;
create sequence mdi_id_seq start with 1 increment by 50;
create sequence pe_config_id_seq start with 1 increment by 50;
create sequence pe_config_item_id_seq start with 1 increment by 50;
create sequence q_group_id_seq start with 1 increment by 50;
create sequence q_permissions_id_seq start with 1 increment by 50;
create sequence q_sch_task_prop_id_seq start with 1 increment by 50;
create sequence q_scheduled_task_config_id_seq start with 1 increment by 50;
create sequence q_user_group_id_seq start with 1 increment by 50;
create sequence q_user_id_seq start with 1 increment by 50;
create sequence q_workflow_config_id_seq start with 1 increment by 50;

    create table curve_def (
       id bigint not null,
        calendar varchar(16) not null,
        currency varchar(3) not null,
        day_counter varchar(32) not null,
        instance varchar(8),
        interpolator varchar(32) not null,
        name varchar(64),
        pricing_context varchar(32) not null,
        rate_index varchar(32) not null,
        rate_index_tenor varchar(16) not null,
        curve_timestamp timestamp,
        version bigint,
        primary key (id)
    );

    create table curve_underlying_fra (
       id bigint not null,
        calendar varchar(32) not null,
        currency varchar(3) not null,
        date_convention varchar(32) not null,
        day_counter varchar(32) not null,
        quote_type varchar(16) not null,
        tenor varchar(6) not null,
        version bigint,
        eom boolean not null,
        fixing_days integer not null,
        ibor_index varchar(16) not null,
        provider varchar(16) not null,
        term varchar(6) not null,
        primary key (id)
    );

    create table curve_underlying_future (
       id bigint not null,
        calendar varchar(32) not null,
        currency varchar(3) not null,
        date_convention varchar(32) not null,
        day_counter varchar(32) not null,
        quote_type varchar(16) not null,
        tenor varchar(6) not null,
        version bigint,
        contract_name varchar(255) not null,
        convexity_adjustment double,
        eom boolean,
        exchange varchar(255) not null,
        imm_date timestamp,
        primary key (id)
    );

    create table curve_underlying_map (
       underlying_id bigint not null,
        curve_id bigint not null,
        primary key (underlying_id, curve_id)
    );

    create table curve_underlying_money_market (
       id bigint not null,
        calendar varchar(32) not null,
        currency varchar(3) not null,
        date_convention varchar(32) not null,
        day_counter varchar(32) not null,
        quote_type varchar(16) not null,
        tenor varchar(6) not null,
        version bigint,
        eom boolean not null,
        fixing_days integer not null,
        ibor_index varchar(16) not null,
        provider varchar(16) not null,
        primary key (id)
    );

    create table curve_underlying_swap (
       id bigint not null,
        calendar varchar(32) not null,
        currency varchar(3) not null,
        date_convention varchar(32) not null,
        day_counter varchar(32) not null,
        quote_type varchar(16) not null,
        tenor varchar(6) not null,
        version bigint,
        fixed_leg_frequency varchar(16) not null,
        float_leg_frequency varchar(16) not null,
        float_leg_index varchar(16) not null,
        provider varchar(32) not null,
        primary key (id)
    );

    create table frtb_samr_commodity_info (
       id varchar(36) not null,
        bucket integer,
        delivery_location varchar(32),
        grade varchar(12),
        product varchar(64),
        trade_identifier varchar(32) not null,
        underlying varchar(64),
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_credit_issuer_info (
       id varchar(36) not null,
        currency varchar(3) not null,
        trade_identifier varchar(32) not null,
        bond_or_cds varchar(6),
        bucket integer,
        issuer varchar(64),
        issuer_id varchar(64),
        rating varchar(16),
        sector varchar(64),
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_drc_non_sec (
       id varchar(36) not null,
        currency varchar(3) not null,
        trade_identifier varchar(32) not null,
        bucket integer not null,
        long_short varchar(8) not null,
        maturity double not null,
        mtm double not null,
        notional double not null,
        product varchar(64) not null,
        rating varchar(8) not null,
        sensitivity_type varchar(16) not null,
        issuer varchar(64) not null,
        issuer_id varchar(64) not null,
        seniority varchar(16) not null,
        strike double,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_drc_sec_ctp (
       id varchar(36) not null,
        currency varchar(3) not null,
        trade_identifier varchar(32) not null,
        bucket integer not null,
        long_short varchar(8) not null,
        maturity double not null,
        mtm double not null,
        notional double not null,
        product varchar(64) not null,
        rating varchar(8) not null,
        sensitivity_type varchar(16) not null,
        credit_index varchar(64) not null,
        region varchar(16) not null,
        securitization boolean not null,
        series integer not null,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_drc_sec_non_ctp (
       id varchar(36) not null,
        currency varchar(3) not null,
        trade_identifier varchar(32) not null,
        bucket integer not null,
        long_short varchar(8) not null,
        maturity double not null,
        mtm double not null,
        notional double not null,
        product varchar(64) not null,
        rating varchar(8) not null,
        sensitivity_type varchar(16) not null,
        bond_or_cds varchar(12) not null,
        category varchar(32) not null,
        portfolio varchar(64) not null,
        region varchar(16) not null,
        seniority varchar(16) not null,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_eq_classification (
       id varchar(36) not null,
        bucket integer,
        market_cap varchar(16),
        region varchar(32),
        sector varchar(128),
        primary key (id)
    );

    create table frtb_samr_equity_info (
       id varchar(36) not null,
        bucket integer,
        ccy varchar(3),
        economy varchar(36),
        issuer varchar(36),
        market_cap varchar(16),
        product varchar(36),
        repo char(255),
        sector varchar(64),
        source varchar(16),
        trade_id varchar(64),
        trade_identifier varchar(80),
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_h_result_rows (
       id varchar(36) not null,
        amount double,
        amount_base double,
        amount_base_high_corr double,
        amount_base_low_corr double,
        amount_high_corr double,
        amount_low_corr double,
        base_currency varchar(12),
        currency varchar(12),
        parent_key varchar(64),
        result_name varchar(16),
        po_results_id varchar(36),
        hierarchy_order integer,
        primary key (id)
    );

    create table frtb_samr_hierarchy (
       id varchar(36) not null,
        book varchar(16),
        desk varchar(16),
        location varchar(16),
        po varchar(16),
        product varchar(32),
        source varchar(16),
        trade_id varchar(64),
        trade_identifier varchar(80),
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_inter_res (
       id varchar(36) not null,
        currency varchar(16) not null,
        rate_index varchar(16) not null,
        result_data blob,
        risk_class varchar(16) not null,
        sensitivity_type varchar(16) not null,
        po_results_id varchar(36),
        inter_results_order integer,
        primary key (id)
    );

    create table frtb_samr_market_quote (
       id varchar(36) not null,
        quote_name varchar(36),
        quote_value double,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_po_results (
       id varchar(36) not null,
        po varchar(32) not null,
        total_risk_charge double,
        samr_results_id varchar(36),
        po_order integer,
        primary key (id)
    );

    create table frtb_samr_residuals (
       id varchar(36) not null,
        currency varchar(3) not null,
        trade_identifier varchar(32) not null,
        notional double not null,
        sensitivity_type varchar(16) not null,
        type integer not null,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_results (
       id varchar(36) not null,
        currency varchar(3) not null,
        total_risk_charge double,
        workflow_id varchar(36),
        primary key (id)
    );

    create table frtb_samr_risk_class_results (
       id varchar(36) not null,
        curvature double,
        delta double,
        drc_non_sec double,
        drc_sec_ctp double,
        drc_sec_non_ctp double,
        res_type1 double,
        res_type2 double,
        risk_class varchar(16),
        vega double,
        po_results_id varchar(36),
        risk_class_results_order integer,
        primary key (id)
    );

    create table frtb_samr_trade_sensitivity (
       id varchar(36) not null,
        currency varchar(3) not null,
        trade_identifier varchar(32) not null,
        basis char(255),
        rate_index varchar(16),
        inflation char(255),
        mtm_base double,
        mtm_1bp_down double,
        mtm_1bp_up double,
        repo char(255),
        sensitivity double,
        sensitivity_type varchar(16) not null,
        tenor varchar(8),
        term varchar(8),
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table frtb_samr_validation_messages (
       id varchar(36) not null,
        validation_messages blob,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table market_data_set (
       id bigint not null,
        name varchar(32),
        primary key (id)
    );

    create table mdi_discount_curve (
       id bigint not null,
        currency varchar(3),
        product_type varchar(32),
        type varchar(255),
        market_data_set bigint not null,
        discount_curve_id bigint not null,
        primary key (id)
    );

    create table mdi_forward_curve (
       id bigint not null,
        currency varchar(3),
        product_type varchar(32),
        type varchar(255),
        market_data_set bigint not null,
        forward_curve_id bigint not null,
        primary key (id)
    );

    create table name_value_ref_data (
       name varchar(64) not null,
        ref_type varchar(64) not null,
        seq integer not null,
        isDefault boolean,
        val varchar(64),
        primary key (name, ref_type, seq)
    );

    create table pricing_context_config (
       name varchar(32) not null,
        currency varchar(3),
        time_zone varchar(16),
        pricing_engine_config bigint,
        quote_set varchar(16),
        primary key (name)
    );

    create table pricing_engine_config (
       id bigint not null,
        pricing_engine_config_name varchar(32) not null,
        primary key (id)
    );

    create table pricing_engine_config_item (
       id bigint not null,
        currency varchar(3) not null,
        pricing_engine_name varchar(32) not null,
        product_type varchar(32) not null,
        discount_curve_id bigint not null,
        pricing_engine_config_id bigint not null,
        primary key (id)
    );

    create table q_exec_info (
       id bigint not null,
        exec_type_id integer,
        exec_timestamp timestamp,
        sch_task_id bigint,
        valuation_timestamp timestamp,
        organization_id bigint not null,
        user_id bigint not null,
        workflow_id varchar(36),
        primary key (id)
    );

    create table q_organization (
       id bigint not null,
        active char(255),
        name varchar(32),
        primary key (id)
    );

    create table q_permissions (
       id bigint not null,
        name varchar(32) not null,
        type varchar(16) not null,
        primary key (id)
    );

    create table q_scheduled_task_config (
       id bigint not null,
        cronExpression varchar(64) not null,
        enabled char(255),
        name varchar(32) not null,
        type varchar(32) not null,
        organization_id bigint not null,
        primary key (id)
    );

    create table q_scheduled_task_properties (
       id bigint not null,
        name varchar(32) not null,
        val varchar(32) not null,
        scheduled_task_id bigint,
        primary key (id)
    );

    create table q_user (
       id bigint not null,
        active char(255),
        email varchar(32) not null,
        name varchar(32) not null,
        password varchar(256) not null,
        organization_id bigint not null,
        primary key (id)
    );

    create table q_user_group (
       id bigint not null,
        active char(255),
        name varchar(32) not null,
        parent_id bigint,
        primary key (id)
    );

    create table q_user_group_map (
       user_id bigint not null,
        group_id bigint not null,
        primary key (user_id, group_id)
    );

    create table q_workflow_config (
       id bigint not null,
        cronExpression varchar(64) not null,
        enabled char(255),
        name varchar(32) not null,
        process varchar(32) not null,
        organization_id bigint not null,
        primary key (id)
    );

    create table q_workflow_instance (
       id varchar(36) not null,
        deleted char(255),
        excel_date integer,
        status integer,
        organization_id bigint not null,
        workflow_config_id bigint not null,
        primary key (id)
    );

    create table q_workflow_instance_properties (
       id varchar(36) not null,
        file_or_dir_name varchar(64) not null,
        path varchar(512) not null,
        workflow_instance_id varchar(36) not null,
        primary key (id)
    );

    create table quote (
       quote_name varchar(128) not null,
        quote_set_name varchar(16) not null,
        timestamp timestamp not null,
        ask double,
        bid double,
        close double,
        open double,
        version bigint,
        primary key (quote_name, quote_set_name, timestamp)
    );

    create table quote_name (
       quote_name varchar(128) not null,
        type varchar(16),
        primary key (quote_name)
    );

    create table quote_set (
       quote_set_name varchar(16) not null,
        description varchar(128),
        parent varchar(16),
        primary key (quote_set_name)
    );

    create table saccr_collateral (
       id varchar(36) not null,
        clearing char(255),
        collateral_balance double,
        col_call_freq integer,
        counterparty varchar(64) not null,
        csa_id varchar(64),
        dispute char(255),
        haircut double,
        ia_cpty double,
        ia_po double,
        key_id varchar(64) not null,
        la_id varchar(64),
        liquid char(255),
        mta_cpty double,
        mta_po double,
        nica double,
        th_cpty double,
        th_po double,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table saccr_market_quote (
       id varchar(36) not null,
        quote_name varchar(36),
        quote_value double,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table saccr_req_trade_attrs (
       id varchar(36) not null,
        asset_class varchar(16) not null,
        buy_sell char(255),
        csa_name char(255),
        description varchar(256),
        early_termination_date char(255),
        end_date char(255),
        la_name char(255),
        legal_entity char(255),
        next_exercise_date char(255),
        notional char(255),
        notional_ccy char(255),
        npv char(255),
        npv_ccy char(255),
        option_type char(255),
        otc_type char(255),
        pay_ccy char(255),
        pay_notional char(255),
        payoff_amount char(255),
        pay_ref_idx char(255),
        payoff_ccy char(255),
        po char(255),
        product char(255),
        product_name varchar(64) not null,
        put_call char(255),
        quantity char(255),
        quanto_fx_rate char(255),
        rec_ccy char(255),
        rec_notional char(255),
        rec_ref_idx char(255),
        settlement_type char(255),
        source_system char(255),
        start_date char(255),
        strike char(255),
        strike2 char(255),
        sub_cls char(255),
        tick_size char(255),
        trade_id char(255),
        underlying_price char(255),
        underlying_product char(255),
        primary key (id)
    );

    create table saccr_supervisory_parameters (
       id varchar(36) not null,
        asset_class varchar(8) not null,
        correlation double,
        sn_or_index varchar(16) not null,
        sub_class varchar(64),
        supervisory_factor double,
        supervisory_option_vol double,
        primary key (id)
    );

    create table saccr_trade (
       id varchar(36) not null,
        buy_sell varchar(4),
        contract_type varchar(16) not null,
        csa_name varchar(64),
        early_termination_date timestamp,
        end_date timestamp not null,
        exercise_type varchar(16),
        la_name varchar(64),
        legal_entity varchar(64) not null,
        next_exercise_date timestamp,
        notional double,
        notional_ccy varchar(3),
        npv double not null,
        npv_ccy varchar(3) not null,
        option_type varchar(16),
        pay_ccy varchar(3),
        pay_notional double,
        payoff_amount double,
        pay_rate_idx varchar(16),
        payoff_ccy varchar(3),
        po varchar(64) not null,
        product_name varchar(64) not null,
        put_call varchar(4),
        quantity double,
        quanto_fx_rate double,
        rec_ccy varchar(3),
        rec_notional double,
        rec_rate_idx varchar(16),
        seq integer not null,
        settlement_type varchar(16),
        source_system varchar(64) not null,
        start_date timestamp not null,
        strike double,
        strike2 double,
        sub_cls varchar(64),
        tick_size integer,
        trade_id varchar(64) not null,
        underlying_price double,
        underlying_product varchar(64),
        workflow_id varchar(36) not null,
        primary key (id)
    );

    create table saccr_validation_messages (
       id varchar(36) not null,
        validation_messages blob,
        workflow_id varchar(36) not null,
        primary key (id)
    );

    alter table curve_def 
       add constraint UKaev4uyee2duwb09x61pmbr11i unique (currency, rate_index, rate_index_tenor);

    alter table market_data_set 
       add constraint UK4ggf9cdr62yjdoyql20nd6sy9 unique (name);

    alter table pricing_engine_config 
       add constraint UKn1b782bioijlme2pv42gjxta7 unique (pricing_engine_config_name);

    alter table q_organization 
       add constraint UKlegp64bixmrfcqystyhlguboc unique (name);

    alter table q_permissions 
       add constraint UK7sdnj1yrl5nv8p8erjpvf1gmk unique (type, name);

    alter table q_scheduled_task_config 
       add constraint UK51nwlwmys7tpvh3t0vo0myevl unique (name, organization_id);

    alter table q_scheduled_task_properties 
       add constraint UKlisoympahecack3y4a05cvuew unique (scheduled_task_id, name);

    alter table q_user 
       add constraint UKgqo8x4cl5den5yqfh1ho32tdj unique (email);

    alter table q_user_group 
       add constraint UKf2l3gabe5ben1sm8o1089n1gx unique (name);

    alter table q_workflow_config 
       add constraint UK8mky6nxug42cw2d6j22irc789 unique (name, organization_id, process);

    alter table frtb_samr_commodity_info 
       add constraint FKi4mennren3gbmhvw2a85wokkk 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_credit_issuer_info 
       add constraint FKafhx3nkv9f7hfs6piqe6ddfbj 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_drc_non_sec 
       add constraint FK1tevjur160yj3ffox8nwycngh 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_drc_sec_ctp 
       add constraint FKbugojh1mfcwnn0eailsu6c4v8 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_drc_sec_non_ctp 
       add constraint FKg7wxlsd6nchdxdlt4o5dkvqqv 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_equity_info 
       add constraint FKfqil81tm617c2nc4fvelkuap1 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_h_result_rows 
       add constraint FKhikjlvmd61elc7kwo94ru1pds 
       foreign key (po_results_id) 
       references frtb_samr_po_results;

    alter table frtb_samr_hierarchy 
       add constraint FK8xml5kdcidy014g2fthv689jw 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_inter_res 
       add constraint FK35l078ck0wdbtoqft6ms2elxn 
       foreign key (po_results_id) 
       references frtb_samr_po_results;

    alter table frtb_samr_market_quote 
       add constraint FK6sdtxfpmaogx8qsi63exsrn01 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_po_results 
       add constraint FK29bqexkxb3px4jy0civ6kn83j 
       foreign key (samr_results_id) 
       references frtb_samr_results;

    alter table frtb_samr_residuals 
       add constraint FK338p2h55fk0onnbuxosoptblk 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_results 
       add constraint FKep4epnlxid3sx4mo8mtuuj7ro 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_risk_class_results 
       add constraint FK2nggnpgdwmo8466x1ahah5om0 
       foreign key (po_results_id) 
       references frtb_samr_po_results;

    alter table frtb_samr_trade_sensitivity 
       add constraint FKh3lp918x9gxyuk7hb51mek6oo 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table frtb_samr_validation_messages 
       add constraint FKq5ymqdrtj8vfm4a5g65yaxm0d 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table mdi_discount_curve 
       add constraint FK9706njapv4p95mfnlgncjj23p 
       foreign key (market_data_set) 
       references market_data_set;

    alter table mdi_discount_curve 
       add constraint FKqsdwmnw0p4qpykpiqpogl7k3g 
       foreign key (discount_curve_id) 
       references curve_def;

    alter table mdi_forward_curve 
       add constraint FK4wng78r7m1ldro2wbty0x3iaw 
       foreign key (market_data_set) 
       references market_data_set;

    alter table mdi_forward_curve 
       add constraint FKf3eg9oglb45otxnd3q785k596 
       foreign key (forward_curve_id) 
       references curve_def;

    alter table pricing_context_config 
       add constraint FK85bsy0cpw2vdwgwwbtwvaagp0 
       foreign key (pricing_engine_config) 
       references pricing_engine_config;

    alter table pricing_context_config 
       add constraint FK4r3x6pv7lin239m3d7bjxs3vw 
       foreign key (quote_set) 
       references quote_set;

    alter table pricing_engine_config_item 
       add constraint FKjdfgj8s0nrlgvwlx79bp0t2yg 
       foreign key (discount_curve_id) 
       references curve_def;

    alter table pricing_engine_config_item 
       add constraint FKchie4y8lm8gp8fnk0n3h4f08t 
       foreign key (pricing_engine_config_id) 
       references pricing_engine_config;

    alter table q_exec_info 
       add constraint FK1kikj7xdgi8caly7wu9d4vccc 
       foreign key (organization_id) 
       references q_organization;

    alter table q_exec_info 
       add constraint FKogoffgs76vhuuipacuo2b18pq 
       foreign key (user_id) 
       references q_user;

    alter table q_exec_info 
       add constraint FKbfjs2nbcepashu6p2kkcn74c1 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table q_scheduled_task_config 
       add constraint FKq8f70oras37mpck4qcey30fl0 
       foreign key (organization_id) 
       references q_organization;

    alter table q_scheduled_task_properties 
       add constraint FKgbov9jlot1op5uwrug64rsg17 
       foreign key (scheduled_task_id) 
       references q_scheduled_task_config;

    alter table q_user 
       add constraint FKguxh2xystf9lf9qns6php1n7w 
       foreign key (organization_id) 
       references q_organization;

    alter table q_user_group 
       add constraint FKghdganfxeknrvlmeguhcfag6j 
       foreign key (parent_id) 
       references q_user_group;

    alter table q_user_group_map 
       add constraint FKcn36fgyxdq9a1mny0m79encm7 
       foreign key (group_id) 
       references q_user_group;

    alter table q_user_group_map 
       add constraint FKgiknxmrjt8jll4cqjlg75ovv8 
       foreign key (user_id) 
       references q_user;

    alter table q_workflow_config 
       add constraint FK4xulwbv8w272pd48ff6hsodoq 
       foreign key (organization_id) 
       references q_organization;

    alter table q_workflow_instance 
       add constraint FKk9ods3867hidfsxphtndqqr7f 
       foreign key (organization_id) 
       references q_organization;

    alter table q_workflow_instance 
       add constraint FK8ymf9p1aptgadbevquqp0v4lk 
       foreign key (workflow_config_id) 
       references q_workflow_config;

    alter table q_workflow_instance_properties 
       add constraint FK1pgomg6qnqct0pr77jvww2kpd 
       foreign key (workflow_instance_id) 
       references q_workflow_instance;

    alter table quote 
       add constraint FKre2kka5u7u86f3t6ustjehw2q 
       foreign key (quote_name) 
       references quote_name;

    alter table quote 
       add constraint FK8e3cjooog8rr7qdx4uytnne4h 
       foreign key (quote_set_name) 
       references quote_set;

    alter table saccr_collateral 
       add constraint FK2hei487f62ujfcqwp91v0kpt 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table saccr_market_quote 
       add constraint FK44r9fjp4e2acl0jpsvfldj9rh 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table saccr_trade 
       add constraint FK2026mbwpwsvpshfbgt2fk61v1 
       foreign key (workflow_id) 
       references q_workflow_instance;

    alter table saccr_validation_messages 
       add constraint FK31ybnrr1ijccouevrwtyxc85w 
       foreign key (workflow_id) 
       references q_workflow_instance;

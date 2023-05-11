package com.quark.db.schema;

import com.quark.bean.util.BeanIntrospector;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.schema.TargetType;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumSet;
import java.util.Properties;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SchemaGenerator {

	private static final String JDBC_PASSWORD = "jdbc.password";
	private static final String JDBC_USERNAME = "jdbc.username";
	private static final String JDBC_URL = "jdbc.url";
	private static final String JDBC_DRIVER_NAME = "jdbc.driver.name";
	private static final String HIBERNATE_DIALECT = "hibernate.dialect";
	public static final String H_2_SCHEMA_SQL = "h2_schema.sql";

	private static void generate(String sysPropsFile) throws SQLException, ClassNotFoundException, IOException {

		Properties p = new Properties();
		p.load(new FileInputStream(sysPropsFile));
        ServiceRegistry serviceRegistry;
        MetadataImplementor metadataImplementor;

		Connection con = null;
		try {
			con = getDBConnection(p);
			MetadataSources metadata = new MetadataSources(new StandardServiceRegistryBuilder()
					.applySetting(HIBERNATE_DIALECT, p.getProperty(HIBERNATE_DIALECT)).build());

			for (Class c : BeanIntrospector.getHibernateClasses()) {
				metadata.addAnnotatedClass(c);
			}

			try {
				Files.delete(Paths.get(H_2_SCHEMA_SQL));
			} catch (IOException e) {
				/*
				 * The file did not exist...
				 * we do nothing.
				 */
			}

            SchemaExport schemaExport = new SchemaExport();
			schemaExport.setDelimiter(";");
            //EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.STDOUT);
			EnumSet<TargetType> targetTypes = EnumSet.of(TargetType.SCRIPT);
			schemaExport.setOutputFile(H_2_SCHEMA_SQL);
			schemaExport.setFormat(true);
			schemaExport.create(targetTypes, metadata.buildMetadata());
		} finally {
			if (con != null) {
				con.close();
			}
		}
	}

	private static Connection getDBConnection(Properties p) throws ClassNotFoundException, SQLException {
		Class.forName(p.getProperty(JDBC_DRIVER_NAME));
		return DriverManager.getConnection(p.getProperty(JDBC_URL), p.getProperty(JDBC_USERNAME),
				p.getProperty(JDBC_PASSWORD));
	}

	public static void main(String[] args) throws Exception {
		String sysPropsFile = System.getProperty("user.home") + "/system.properties";
		if (args.length > 0 && StringUtils.isNoneBlank(args[0]) && new File(args[0]).exists()) {
			sysPropsFile = args[0];
		}
		log.info("Loading system properties from : {} ", sysPropsFile);
		generate(sysPropsFile);
	}
}

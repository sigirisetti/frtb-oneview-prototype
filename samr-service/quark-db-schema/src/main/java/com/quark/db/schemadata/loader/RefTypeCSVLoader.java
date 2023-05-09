package com.quark.db.schemadata.loader;

import com.quark.marketdata.dao.ReferenceDataDao;
import com.quark.refdata.model.NameValuePair;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
public class RefTypeCSVLoader extends DirectoryWalker {

	@Autowired
	private ReferenceDataDao dao;

	@Value("${core.data.csv.reftypes.dir:../data/csv/reftypes}")
	private String refTypesDir;

	public RefTypeCSVLoader() {
		super(FileFilterUtils.suffixFileFilter(".csv"), -1);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public void handleFile(File file, int depth, Collection results) throws IOException {
		String fn = file.getName();
		String refType = FilenameUtils.removeExtension(fn);
		List<String> lines = FileUtils.readLines(file);
		List<NameValuePair> pairs = new ArrayList<>();
		for (int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if (StringUtils.isNoneBlank(line)) {
				String[] tokens = line.split(",");
				String name = StringUtils.isNotBlank(tokens[0]) ? tokens[0] : "";
				String value = StringUtils.isNotBlank(tokens[1]) ? tokens[1] : "";
				if (tokens.length > 2) {
					pairs.add(new NameValuePair(refType, i + 1, name, value, "1".equals(tokens[2])));
				} else if (tokens.length > 1) {
					pairs.add(new NameValuePair(refType, i + 1, name, value, Boolean.FALSE));
				} else {
					pairs.add(new NameValuePair(refType, i + 1, name, null, Boolean.FALSE));
				}
			}
		}

		dao.save(refType, pairs);
	}

	public List<File> load() {
		List<File> files = new ArrayList<>();
		File directory = new File(refTypesDir);
		try {
			walk(directory, files);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return files;
	}
}
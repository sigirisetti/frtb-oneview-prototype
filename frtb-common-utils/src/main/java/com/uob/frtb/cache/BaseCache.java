package com.uob.frtb.cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BaseCache {

	private static Map<Key, Object> mapCache = new ConcurrentHashMap<Key, Object>();

	public static Object cache(Key k, Object o) {
		return mapCache.put(k, o);
	}

	public static Object lookup(Key k) {
		return mapCache.get(k);
	}

	public static void clear() {
		mapCache.clear();
	}

	public static class Key {
		private String name;

		public Key(String name) {
			this.name = name;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Key other = (Key) obj;
			if (name == null) {
				if (other.name != null)
					return false;
			} else if (!name.equals(other.name))
				return false;
			return true;
		}
	}
}

package com.monarchapis.driver.hash;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import com.google.common.collect.Lists;
import com.monarchapis.driver.exception.ApiException;

public class RequestHasherRegistry {
	private List<RequestHasher> hashers;
	private Map<String, RequestHasher> lookup;

	public RequestHasherRegistry() {
		lookup = new HashMap<String, RequestHasher>();
	}

	public RequestHasherRegistry(List<RequestHasher> hashers) {
		setRequestHashers(hashers);
	}

	public RequestHasherRegistry(RequestHasher... hashers) {
		setRequestHashers(Lists.newArrayList(hashers));
	}

	@Inject
	public void setRequestHashers(List<RequestHasher> hashers) {
		Map<String, RequestHasher> lookup = new HashMap<String, RequestHasher>();

		for (RequestHasher hasher : hashers) {
			lookup.put(hasher.getName(), hasher);
		}

		this.lookup = lookup;
		this.hashers = Collections.unmodifiableList(new ArrayList<RequestHasher>(hashers));
	}

	public List<RequestHasher> getRequestHashers() {
		return hashers;
	}

	public RequestHasher getRequestHasher(String name) {
		RequestHasher ret = lookup.get(name);

		if (ret == null) {
			throw new ApiException("Request hasher " + name + " is not registered");
		}

		return ret;
	}
}

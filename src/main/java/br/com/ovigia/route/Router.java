package br.com.ovigia.route;

import java.util.ArrayList;
import java.util.List;

public class Router {
	private List<Route<?, ?>> routes = new ArrayList<>();

	public <T, V> void add(Route<T, V> route) {
		routes.add(route);
	}

	public List<Route<?, ?>> getRoutes() {
		return routes;
	}
}

package br.com.ovigia.route;

import java.util.ArrayList;
import java.util.List;

class Router {
	private List<Route<?, ?>> routes = new ArrayList<>();

	public void registry(RoutesRegister register) {
		routes.forEach(route -> register.registry(route));
	}

	public <T, V> void add(Route<T, V> route) {
		routes.add(route);
	}

}

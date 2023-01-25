package server;

import models.Route;
import repos.RouteRepository;
import user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * finds the route to execute
 */
public class FindRoute extends Command {

    @Override
    public String execute(User user, Server server, String message) {
        RouteRepository routeRepository = new RouteRepository();
        List<Route> routeList;
        routeList = routeRepository.findByName(message);
        Random rand = new Random();
        Route randomElement = routeList.get(rand.nextInt(routeList.size()));
        return randomElement.getPath();
    }
}
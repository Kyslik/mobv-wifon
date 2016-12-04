package com.stu.fei.mobv;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ezia on 04.12.2016.
 */

public class Navigation {

    static Location actualLocation = new Location();
    static Location destinationLocation = new Location();
    static List<NavigationFragmentAdapter.Step> navigationSteps = new LinkedList<>();

    public static List navigate(Location actualLocation, String destination) {

        Integer i = 0;
        navigationSteps.clear();

        if(destination.contains("cpu")){
            destinationLocation.block = "D";
            destinationLocation.level = "0";
        }
        else if(destination.contains("bU")){
            destinationLocation.block = "B";
            destinationLocation.level = "0";
        }

        destinationLocation.block = destination.replaceAll("[^A-Za-z]+", "");
        destinationLocation.level = destination.replaceAll("[^0-9]", "");
        if(destinationLocation.level == ""){
            destinationLocation.level = "0";
        }

        if(destinationLocation.block.contains("AB") || destinationLocation.block.contains("BC") || destinationLocation.block.contains("CD") || destinationLocation.block.contains("DE")){
            if(!actualLocation.level.equals("0")) {
                navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(actualLocation.block.toString(), "0"), "Pouzite vytah na prizemie"));
            }
            if(!actualLocation.block.equals(destinationLocation.block.substring(1))){
                navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(destinationLocation.block.toString().substring(1), "0"), "Pokracuj na blok: "+ destinationLocation.block.toString().substring(1)));
            }
            navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(destinationLocation.block.toString().substring(1), "0"), "Vstup do miestnosti: "+ destination.toString()));
        }

        else {

            if (!actualLocation.block.equals(destinationLocation.block)) {
                if (!actualLocation.level.equals("0")) {
                    navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(actualLocation.block.toString().substring(0,1), "0"), "Pouzite vytah na prizemie"));
                }
                navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(destinationLocation.block.toString().substring(0,1), "0"), "Pokracujte na blok " + destinationLocation.block.toString().substring(0,1)));

                actualLocation.block = destinationLocation.block;
                actualLocation.level = "0";
            }

            if (!actualLocation.level.equals(destinationLocation.level.substring(0,1))) {
                navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(destinationLocation.block.toString().substring(0,1), destinationLocation.level.toString().substring(0,1)), "Chodte na " + destinationLocation.level.toString().substring(0,1)+ " poschodie "));
            }

            navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(destinationLocation.block.toString(), destinationLocation.level.toString().substring(0,1)), "Chodte do miestnosti: " + destination.toString()));

        }

        if(navigationSteps.isEmpty()){
            navigationSteps.add(new NavigationFragmentAdapter.Step(i++, getLabel(actualLocation.block.toString(), actualLocation.level.toString()), "Nepodarilo sa najst miestnost."));
        }

        return navigationSteps;
    }


    public static String getLabel(String block, String level) {
        String name = new String("Blok");
        name += " " + block;
        if (level == "0") {
            name += " - prízemie";
        }
        else {
            name += " - " + level + " poschodie";
        }

        return name;
    }
}


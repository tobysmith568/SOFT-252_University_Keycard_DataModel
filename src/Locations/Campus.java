/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Locations;

import Locations.States.EmergencyStatus;
import java.util.ArrayList;

/**
 *
 * @author tsmith10
 */
public class Campus extends Location{
    private String name;
    private ArrayList<Building> buildings = new ArrayList<Building>();
    
    public Campus(String name){
        this.name = name;
    }
    
    public void CreateBuilding(Campus campus, String name, String shortCode){
        buildings.add(new Building(campus, name, shortCode));
    }

    @Override
    public void SetRoomState(EmergencyStatus newState) {
        for (int i = 0; i < buildings.size(); i++) {
            buildings.get(i).SetRoomState(newState);
        }
    }

    public String GetName() {
        return name;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Locations;

import Control.RoomFactory;
import Locations.States.LocationState;
import java.util.HashMap;
import java.util.Iterator;

/**
 *
 * @author Student
 */
public class Floor extends Location {
    private Building building;
    private String floorNumber;
    private HashMap<String, Room> rooms = new HashMap<>();
    
    /**
     *
     * @param floorNumber
     */
    public Floor(String floorNumber){
        this.floorNumber = this.fullName = floorNumber;        
    }

    /**
     *
     * @return
     */
    public Building GetBuilding() {
        return this.building;
    }

    /**
     *
     * @return
     */
    public String GetFloorNumber() {
        return floorNumber;
    }  
    
    /**
     *
     * @param name
     * @return
     */
    @Override
    public Room GetChild(String name){
        if (!rooms.containsKey(name))
            return null;
        else
            return rooms.get(name);
    }
    
    /**
     *
     * @return
     */
    @Override
    public Room[] GetAllChildren(){
        return rooms.values().toArray(new Room[0]);
    }

    /**
     *
     * @param newState
     */
    @Override
    protected void ActualSetRoomState(LocationState newState) {
        super.ActualSetRoomState(newState);
        Iterator iterator = rooms.values().iterator();
        while (iterator.hasNext()){
            ((Room)iterator.next()).ActualSetRoomState(newState);
        }
    }
    
    /**
     *
     * @param type
     * @return
     */
    public Room AddRoom(RoomType type) {
        Room room = RoomFactory.Create(Integer.toString(rooms.size()), type, this);
        rooms.put(room.GetNumber(), room);
        room.SetFullName(this.fullName + room.GetNumber());
        return room;
    }
}

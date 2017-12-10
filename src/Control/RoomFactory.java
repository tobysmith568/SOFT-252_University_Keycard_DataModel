/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Locations.Floor;
import Locations.Room;
import Locations.RoomType;
import static Locations.States.LocationState.NOEMERGENCY;

/**
 *
 * @author Toby
 */
public class RoomFactory {
    
    public static Room Create(String number, RoomType type, Floor floor){
        Room room = new Room(number);
        
        room.SetFloor(floor);        
        room.SetRoomType(type);
        room.SetRoomState(NOEMERGENCY);
        
        return room;
    }
}
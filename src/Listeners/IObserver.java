/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Listeners;

import Locations.Location;
import Locations.Room;
import Locations.States.LocationState;
import People.Keycard;

/**
 *
 * @author Toby
 */
public interface IObserver {
    public void ObservedUpdate(Location location, LocationState locationState);
    public void ObservedUpdate(Keycard keycard, Room room, boolean wasSuccessful);
}

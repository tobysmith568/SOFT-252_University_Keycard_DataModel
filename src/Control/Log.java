/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Control;

import Listeners.IAccessObserver;
import Listeners.ILogObserver;
import Listeners.ILogSubject;
import Locations.Location;
import Locations.Room;
import Locations.States.LocationState;
import People.Keycard;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import Listeners.IStateObserver;
import static Locations.States.LocationState.EMERGENCY;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Logs messages to the console as well as to a file.
 * A new file is used for each day and is named in the format "Log for dd-MM-yy".
 * @author Student
 */
public class Log implements IStateObserver, IAccessObserver, ILogSubject, Serializable{
    
    private static Log singleton;
    private final DateTimeFormatter logMessageFormat;
    private final DateTimeFormatter dailyFileFormat;
    private final DateTimeFormatter emergencyFolderFormat;
    
    private final ArrayList<ILogObserver> logObservers;
    private final ArrayList<String> unsavedMessages;
    
    private Log() {
        this.logObservers = new ArrayList<>();
        this.unsavedMessages = new ArrayList<>();
        this.emergencyFolderFormat = DateTimeFormatter.ofPattern("dd-MM-yy_HH-mm-ss");
        this.logMessageFormat = DateTimeFormatter.ofPattern("'['dd/MM/yy'] ['HH:mm:ss']'");
        this.dailyFileFormat = DateTimeFormatter.ofPattern("dd-MM-yy");
    }
    
    /**
     * Gets the Logger instance.
     * @return The Logger instance
     */
    public static Log Logger() {
        if (singleton == null)
            singleton = new Log();
        
        return singleton;
    }
    
    private String LogPrefix() {
        return LocalDateTime.now().format(logMessageFormat);
    }
    
    private void LogAccess(Keycard keycard, Room room, boolean wasSuccessful) {
        String output = LogPrefix() + " " + room.GetFullName() + " (" + room.GetState().GetName() +
                (wasSuccessful ? ") granted access to " : ") denied access to ") +
                keycard.GetName() + " (" + keycard.GetCardID() + ")";
        
        Logger().SendLog(output);
    }
    
    private boolean LogEmergency(Location location, LocationState state, String reason) {
        boolean anyFailures = false;
        String output = LogPrefix() + " " + location.GetFullName() + " is now in the state " + 
                state.GetName() + " for the reason: " + reason;
                
        Logger().SendLog(output);     
        
        //Special logic for if the new state is EMERGENCY
        //Needs to save the current state and log file to an emergency folder
        if (state == EMERGENCY) {
            Path emergencyDirectory = Paths.get("Emergency Logs", "EM_" + LocalDateTime.now().format(emergencyFolderFormat));
            try{
                //Ensure the emergency folder exists
                Files.createDirectories(emergencyDirectory);
                
                //Copy over the current log file
                Path logFile = GetTodaysLogFile();
                Files.copy(logFile, emergencyDirectory.resolve(logFile.getFileName()));
                
                //Save the current locations and keycards
                Data.SaveState(emergencyDirectory.resolve("Current.state").toString(), Data.allCampuses, Data.allKeycards);
            } catch (IOException e) {
                Log.Log("ERROR: " + e.getMessage());
                anyFailures = true;
            }
        }
        return anyFailures;
    }
    
    /**
     * Prints a <code>String</code> to both the console and the current log file.
     * @param message The <code>String</code> to be printed
     */
    public static void Log(String message) {
        String output = Logger().LogPrefix() + " " + message;
        
        Logger().SendLog(output);
    }
    
    private void SendLog(String message) {
        //Each log goes to three places, Log observers, .log file, and the console
        UpdateLogObservers(message);
        Logger().LogToFile(message);
        System.out.println(message);
    }
    
    private boolean LogToFile(String message) {
        boolean anyFailures = false;
        Path logFile = GetTodaysLogFile();
        
        //If there's already 100 pending messages to log in the file, delete the oldest
        if (unsavedMessages.size() == 100)
            unsavedMessages.remove(0);
        
        //Add the current message to log to the list of pending messages
        unsavedMessages.add(0, message);
        
        //For each pending message, try to write them to the .log file
        for (int i = unsavedMessages.size() - 1; i >= 0; i--) {
            try {
                //Ensure the daily .log file directory exists
                if (!Files.exists(logFile.getParent()))
                    Files.createDirectories(logFile.getParent());
                
                //Write the daily .log file exists, if APPEND, else use CREATE when writing the message
                Files.write(logFile, Arrays.asList(message), Files.exists(logFile) ? StandardOpenOption.APPEND : StandardOpenOption.CREATE);
                unsavedMessages.remove(i);
            } catch (IOException e) {
                Log.Log("ERROR: " + e.getMessage());
                anyFailures = true;
                //Break from the for loop to preserve the order of logged messages
                break;
            }
        }
        return anyFailures;
    }

    /**
     * Logs a change in a <code>Location</code>'s state. Prints the log to both
     * the console and the current log file.
     * @param location The <code>Location</code> which has changed state
     * @param locationState The new state of the <code>Location</code>
     * @param reason The reason for the state being changed
     */
    @Override
    public void ObservedStateUpdate(Location location, LocationState locationState, String reason) {
        LogEmergency(location, locationState, reason);
    }

    /**
     * Logs an attempt to access a <code>Room</code>. Prints the log to both
     * the console and the current log file.
     * @param keycard The <code>Keycard</code> used to try and gain access
     * @param room The <code>room</code> the <code>Keycard</code> tried to
     * access
     * @param wasSuccessful If the attempt was successful or not in gaining
     * access
     */
    @Override
    public void ObservedAccessUpdate(Keycard keycard, Room room, boolean wasSuccessful) {
        LogAccess(keycard, room, wasSuccessful);
    }

    /**
     * Adds a new log observer to this object.
     * @param observer The log observer to add
     * @return If the observer was successfully added or not - this can also
     * return <code>false</code> if the observer to add is already an observer
     */
    @Override
    public boolean AddLogObserver(ILogObserver observer) {
        if (logObservers.contains(observer))
            return false;
        else {
            logObservers.add(observer);
            return logObservers.contains(observer);
        }
    }

    /**
     * Removes a log observer from this object.
     * @param observer The log observer to remove
     * @return If the observer was successfully removed
     */
    @Override
    public boolean RemoveLogObserver(ILogObserver observer) {
        return logObservers.remove(observer);
    }

    /**
     * Updates all log observers with a new logged message.
     * @param message The new message which has been logged
     */
    @Override
    public void UpdateLogObservers(String message) {
        logObservers.forEach((observer) -> {
            observer.ObservedLogUpdate(message);
        });
    }
    
    /**
     * Returns the name of today's log file.
     * @return The name of the file
     */
    public Path GetTodaysLogFile() {
        return Paths.get("Daily Logs", "Log for " + LocalDateTime.now().format(dailyFileFormat) + ".log");
    }
}

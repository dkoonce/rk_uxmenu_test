package com.rkbots.tools.app.uxmenu;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

public abstract class FileWatcher {
    /*TODO: Add universal file to watch.
            Add notification event if wanted
            Add universal object if it's serializable.
     */
    private boolean fileFound = false;
    private String fileName;
    private String pathName = "/tmp/";
    //private String extension = ".info"; //Do we want an extension?

    private long interval = 1000; //How often to check 1 second should be a fine default.
    public long getInterval(){
        return interval;
    }
    public void setInterval(long value){
        System.err.println( fileName + " Watcher interval updated " + value);
        interval = value;
    }

    /* Don't use this for binary files right now.
    private boolean binaryFile = false; //If the file is binary, don't try to read it, just check the modification date.
    public boolean getBinaryFile(){
        return binaryFile;
    }
    public void setBinaryFile(boolean value){
        binaryFile = value;
    }
    */

    public String FullFileName(){
        return pathName + fileName;
    }

    private String value = "";
    public String getValue(){
        return value;
    }
    public void setValue(String value){
        //Update watcher file always
        try (PrintWriter valueWriter = new PrintWriter(FullFileName(), "UTF-8")){
            valueWriter.print(value);
            valueWriter.close();
            if(watching) return; //For now, if there is an active watcher, allow the event to update the value.
            //If not watching, update the value directly.
            this.value = value;
        } catch (Exception ex) {
            System.err.println(ex); //Always log the error
        }
    }

    private TimerTask task;
    private Timer timer = new Timer();

    private boolean watching = false;

    protected abstract void hasChanged();

    public void StopWatcher(){
        if(!watching) return;
        timer.cancel();
        timer.purge();
        task.cancel();
        value = "";
        System.err.println( "Watcher Stopping " + FullFileName());
        watching = false;
    }

    public FileWatcher(String statusFile, String path){
        fileName = statusFile;
        pathName = path;

    }

    public void StartWatcher(){
        // monitor a single file, single instance (for now)
        System.err.println( "Watcher Starting " + FullFileName());
        if(watching) return; //Don't allow another watcher to start.
        task = new fileWatcher( new File(FullFileName()) ) {
            protected void onChange( File file ) {
                // here we code the action on a change
                //System.out.println( "File "+ file.getName() +" Changed!" );
                FileReader();
                if(fileFound) {

                    //This is temporary for testing
                    System.err.println("Watcher Value: " + value);

                    //Attempt to test..
                    try {
                        //System.out.print("Testing");
                        hasChanged();
                    } catch (Exception ex) {
                        System.err.println(fileName + " Test FAILED Value: " + value);
                    }
                } else {
                    System.err.println( fileName + " File not Detected");
                }
                fileFound = false;
            }
        };

        // repeat the check every second
        timer.scheduleAtFixedRate( task , 100, interval);
        System.err.println(fileName + " Watcher Started");
        watching = true;
    }

    private void FileReader()
    {
        try {
            Path filePath = new File(FullFileName()).toPath();
            Charset charset = Charset.defaultCharset();
            String newValue = new String(Files.readAllBytes(filePath)); //Get entire file just in case it's JSON
            value = newValue; //Test this if it's changed.
            fileFound = true;
            return;
        } catch (IOException ex) {
            System.err.println(fileName + " not found");
            fileFound = false;
            //} catch (NoSuchFileException ex) {
        } catch (Exception ex) {
            System.err.println(ex);
        }
        //Lets ruin the values if the file is lost or malformed.
        value = "";
    }

    private abstract class fileWatcher extends TimerTask {
        private long timeStamp;
        private File file;

        public fileWatcher( File file ) {
            //System.out.println("Starting fileWatcher");
            this.file = file;
            this.timeStamp = file.lastModified();
        }

        public final void run() {
            long timeStamp = file.lastModified();
            //TODO: Updated a LastModified datetime property that can be read at any time.
            if( this.timeStamp != timeStamp ) {
                this.timeStamp = timeStamp;
                onChange(file);
            }
        }

        protected abstract void onChange( File file );
    }
}

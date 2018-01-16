/*

Matrix Update with Madi Bridge application by Jakob Greif

Define in devices.txt if a Madi Bridge ist connected to the Matrix
if there is a dictionary with type "\madibridgeIN" in "devices.txt" the Device will be created.
MXMadiBridge reads the the Channel Setup, defined in: "madiconfig.txt". There the Channels and Names of 
connected Devices can be definded. 

*/


MXMadiBridge {
	classvar <madiBridgeChannels;
	classvar <MidiDeviceName;
	classvar <MidiDevicePort;
	classvar <madiBridgeMidiOut;
	
	
	
	*init {
	// madiBridgeChannels = Dictionary.new;
	madiBridgeChannels = List.new;
	this.readConfig;
	this.initMIDIConnection;	
	}	
	
	// read Config File and add to List: madiBridgeChannels
	*readConfig {
	var path, arrayfromfile, dict;
		path = MXGlobals.configDir ++ "madiconfig.txt";
		if (File.exists(path)) {
			("\n------------------\nreading" + path + "\n------------------").postln;
			arrayfromfile = File.open(path, "r").readAllString.interpret;
		//	arrayfromfile.postln; 
			if (arrayfromfile.isNil) { "WARNING: no madi bridge Channels declared in midiConfig.txt".postln };
			arrayfromfile.do { arg assoc, i;
				this.addMadiChannelFromArray([ assoc.key] ++ assoc.value );
			} ;
		} {
			"FILE ERROR: monitorpresets.txt not found!".postln; 
		};
		
	MidiDeviceName = "USB MIDI Interface";
	
	}	
	// read config File split name and Channel and add to List	
	*addMadiChannelFromArray {arg array;
	var name, channel;
	name = array[0].asString;
	channel = array[1].asInteger;	
	madiBridgeChannels.add(name -> channel);
	("Madi Bridge Channel added:" + name + channel).postln;
    }
	 
	// initialize Midi connection for USB MIDI Interface
	*initMIDIConnection{
	MidiDeviceName = "USB MIDI Interface";
	MidiDevicePort = "USB MIDI Interface";
	// define Midi Out 
	madiBridgeMidiOut = MIDIOut.findPort(MidiDeviceName, MidiDevicePort);
	}

	//function to Change madiBridge Source Channel. Called by MXDevice madiChannelSV.action
	*changeMadiChannel {
	arg changer;
	//("Change Source Channel on Madi Bridge to" + changer).postln;
	//MadyOutInit has to be done every time sysex is sent
	MidiDeviceName = "USB MIDI Interface";
	MidiDevicePort = "USB MIDI Interface";
	madiBridgeMidiOut = MIDIOut.findPort(MidiDeviceName, MidiDevicePort);
	madiBridgeMidiOut = MIDIOut.newByName(MidiDeviceName, MidiDevicePort);
	//Sending Sysex Messege to MadiBridge to Change Source Channel for 
	// Current Out to be Changed 7->0x5E; 8->0x5F; Change Source to 'changer'
	// for more info Check MIDI Implementation Chart in RME Website: https://www.rme-audio.de/download/madibridge_d.pdf
	
	madiBridgeMidiOut.sysex(Int8Array[0x00, 0xF0, 0x00, 0x20, 0x0D, 0x65, 0x00, 0x20, 0x5E, changer , 0xF7]);
	}			
}

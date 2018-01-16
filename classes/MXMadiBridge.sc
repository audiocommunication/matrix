/*

Matrix Update with Madi Bridge application

read devices.txt for MadiBridge IN/Out Channeling and Madi Bridge Routing

*/


MXMadiBridge {
	classvar <madiBridgeChannels;
	classvar <deviceNameTest;
	classvar <portOutNameTest;
	classvar <madiBridgeMidiOut;
	
	
	
	*init {
	// madiBridgeChannels = Dictionary.new;
	madiBridgeChannels = List.new;
	this.readConfig;
	this.initMIDIConnection;	
	}	
	
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
		
	deviceNameTest = "MT4";
	
	}	
		
	*addMadiChannelFromArray {arg array;
	var name, channel;
	
	name = array[0].asString;
	//channel = array[1].asInteger;
	channel = array[1].asInteger;
		
	// if (madiBridgeChannels.isNil) {madiBridgeChannels.put(}
	
	madiBridgeChannels.add(name -> channel);
	("Madi Bridge Channel added:" + name + channel).postln;
     }

	*initMIDIConnection{
	deviceNameTest = "USB MIDI Interface";
	portOutNameTest = "USB MIDI Interface";
	madiBridgeMidiOut = MIDIOut.findPort(deviceNameTest, portOutNameTest);
	
	// madiBridgeMidiout = MIDIOut.newByName(deviceNameTest, portOutNameTest);
	}

	*changeMadiChannel {
	arg changer;
	//changer.postln;
	// var changer = 5;
	deviceNameTest = "USB MIDI Interface";
	portOutNameTest = "USB MIDI Interface";
	madiBridgeMidiOut = MIDIOut.findPort(deviceNameTest, portOutNameTest);
	madiBridgeMidiOut = MIDIOut.newByName(deviceNameTest, portOutNameTest);
	madiBridgeMidiOut.sysex(Int8Array[0x00, 0xF0, 0x00, 0x20, 0x0D, 0x65, 0x00, 0x20, 0x5F, changer , 0xF7]);
					
	}		
	
//		MIDIClient.destinations;
//		MIDIClient.sources;


/*
//var x_test = 0x07;
// var x_test = 0x06;
var deviceNameTest = "MT4";
var portOutNameTest = "Port 1";
var mtmidiout1;

//mtmidiout1 = MIDIOut.findPort(deviceNameTest, portOutNameTest);
//mtmidiout1 = MIDIOut.newByName(deviceNameTest, portOutNameTest);
// MT4out1.postln
//mtmidiout1.sysex(Int8Array[0x00, 0xF0, 0x00, 0x20, 0x0D, 0x65, 0x00, 0x20, 0x5F, x_test, 0xF7]);


mtmidiout1 = MIDIOut.

MXMIDI.midiModels.postln;
MXMIDI.midiDeviceDict.postln;
MXMIDI.midiDevice.modelMessageDict.postln;
*/		
	
}

//MXMain.init("/Users/matrix/_MATRIX/config/");
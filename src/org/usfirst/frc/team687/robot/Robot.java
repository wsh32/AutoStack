
package org.usfirst.frc.team687.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
    /**
     * This function is run when the robot is first started up and should be
     * used for any initialization code.
     */
	
		Joystick joy;
		Compressor com;
		Encoder encoder;
		Talon leftIntake, rightIntake;
		AutoStacker artic;
		
    public void robotInit() {
    	joy = new Joystick(0);
		com = new Compressor();
		com.start();
		leftIntake = new Talon(6);
		rightIntake = new Talon(7);
		encoder = new Encoder(8,9);
		artic = new AutoStacker(1,0,1,2,3);
    }

    /**
     * This function is called periodically during autonomous
     */
    public void autonomousPeriodic() {

    }

    /**
     * This function is called periodically during operator control
     */
    
    //states for the autostack
    boolean current = false, last = false, pressed = false;
    public void teleopPeriodic() {
    	//button state
        current = joy.getRawButton(1);
        pressed = current && !last;
        last = current;
        
        //do the artic stuff
        artic.setEncoderValue(encoder.getRaw());
        if(pressed)artic.stack();
        artic.run();
    }
    
    /**
     * This function is called periodically during test mode
     */
    public void testPeriodic() {
    
    }
    
}

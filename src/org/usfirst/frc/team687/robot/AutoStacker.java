package org.usfirst.frc.team687.robot;

import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class AutoStacker {
	//State Values
	private int m_state = -1;
	private boolean m_running = false;
	
	//PI Values
	private final double m_kP = 0.2;
	private final double m_kI = 0.0002;
	
	private double p, i, power;
	
	//encoder values
	private final int m_ticks;
	private double m_encode = 0;
	
	//solenoids
	private DoubleSolenoid m_leftSol, m_rightSol;
	
	private CANTalon m_motor;
	 
	//Constructors
	public AutoStacker(int motor)	{
		m_motor = new CANTalon(motor);
		m_leftSol = null;
		m_rightSol = null;
		m_ticks = 256;
	}
	
	public AutoStacker(int motor, int ticks)	{
		m_motor = new CANTalon(motor);
		m_leftSol = null;
		m_rightSol = null;
		m_ticks = ticks;
	}
	
	public AutoStacker(int motor, int leftSol1, int leftSol2, int rightSol1, int rightSol2)	{
		m_motor = new CANTalon(motor);
		m_leftSol = new DoubleSolenoid(leftSol1, leftSol2);
		m_rightSol = new DoubleSolenoid(rightSol1, rightSol2);
		m_ticks = 256;
	}
	
	public AutoStacker(int motor, int leftSol1, int ticks, int leftSol2, int rightSol1, int rightSol2)	{
		m_motor = new CANTalon(motor);
		m_leftSol = new DoubleSolenoid(leftSol1, leftSol2);
		m_rightSol = new DoubleSolenoid(rightSol1, rightSol2);
		m_ticks = ticks;
	}
	
	//To get the place
	public void setEncoderValue(double encode){
		m_encode = encode / m_ticks + 7.5;
	}
	
	//Let's GO!
	public void stack()	{
		m_running = true;
		m_state = 0;
	}
	
	//Heres where the magic happens
	public void run()	{
		if(m_running && m_state!=-1)	{
			double pow = 0;
			double height = 0;
			switch(m_state)	{
			//Case 0: Move to 2 TH (24 in)
			case 0:
				height = 24;
				pow = piLoop(height);
				m_motor.set(pow);
				if(Math.abs(m_encode-height)<.2)	{
					reset();
					m_state = 1;
				}
			
			//Case 1: Open Arms
			case 1:
				m_leftSol.set(DoubleSolenoid.Value.kForward);
				m_rightSol.set(DoubleSolenoid.Value.kForward);
				m_state = 2;
				
			//Case 2: drop to 1.5TH (18 in)
			case 2:
				height = 18;
				pow = piLoop(height);
				m_motor.set(pow);
				if(Math.abs(m_encode-height)<.2)	{
					reset();
					m_state = 3;
				}
				
			//Case 4: close arms
			case 4:
				m_leftSol.set(DoubleSolenoid.Value.kReverse);
				m_rightSol.set(DoubleSolenoid.Value.kReverse);
				m_state = 5;
			
			//Case 5: raise to 2.5 th (30 in)
			case 5:
				height = 30;
				pow = piLoop(height);
				m_motor.set(pow);
				if(Math.abs(m_encode-height)<.2)	{
					reset();
					m_running = false;
					m_state = -1;
				}
				
			default:
				//SHOULD NEVER HAPPEN
				m_running = false;
				
			}
		}	else	{
			m_state = -1;
			m_motor.set(0);
		}
	}
	
	private double piLoop(double desired) {
		double error;
		double power;
		
		error =  desired - m_encode;
		p = error;
		i = i + error;
		power = p * m_kP + i * m_kI;
		
		return power;
	}
	
	private void reset()	{
		i = 0;
	}
}

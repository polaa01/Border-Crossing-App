This is Java application which simulates work of border crossing.
Border crossing has 3 police and 2 customs terminals. Each vehicle must go through police check, then customs check.
Three types of vehicles are supported: Cars, Buses and Trucks. One of each types of terminals is reserved for trucks.

The simulation includes 50 vehicles in total (35 cars, 10 trucks and 5 five buses) which are randomly positioned in queue.
The car can accomodate up to 5 passengers, the bus can accomodate up to 52 passengers, while the truck can accomodate up to 3 passengers.
The possibility that passenger on the bus have a luggage is 70%, while 10% of all luggages contain forbidden items.
There is 50% chance that customs documentation for trucks is needed, and 20% of all trucks are overloaded.

Police/Customs processing for each car and truck takes 0.5 seconds. On the other hand, police/customs processing for each bus takes 0.1 seconds.

3% of documents is invalid (the passenger is removed), if that passenger is a driver, the vehicle is rejected.
If passenger's luggage contains forbidden items, the passenger is removed.

We can display all incidents with reasons (descriptions). Shared file controls terminal status (blocked/active).
Information about penalized passengers are serialized in separate files.

Logger class is used for exception handling. Simulation can be paused/resumed.



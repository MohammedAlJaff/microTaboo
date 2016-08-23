Folder A contains the genome for E.coli O157:H7
Folder B contains the genome for E.coli 0127:H6

Run the following command (Make sure your machine has enough RAM: recommended amount is around 7Gig Ram ) : 

java -Xmx5000m -jar microTaboo.jar A B R 50 0 a 1 s


Expect: in the sub-folder R, you should now find:
A roughly 95mb file called EcoliO157W50k0_I.txt with all 50nt sequences that E.coli 0157:H7 does share with E.coli 0127:H6 for k = 0
A roughly 230mb file called EcoliO157W5kK0_D.txt with all 50nt sequences that E.coli 0157:H7 doesn't share with E.coli 0127:H6 for k = 0
A file called EcoliO157W50k0_Time.txt with the total runtime in milliseconds
 

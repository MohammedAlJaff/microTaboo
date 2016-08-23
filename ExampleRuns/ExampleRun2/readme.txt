Folder A contains the genome for Enterobacteria phage lambda
Folder B contains the genome for E.coli str. K12 substr. MG1655

Run the following command (Make sure your machine has enough RAM: recommended amount is around 7Gig Ram ) : 

java -Xmx5000m -jar microTaboo.jar A B R 80 0 a 1 s

Expect: in the sub-folder R, you should now find:
A roughly 130kb file called lamdaPhageW80k0_I with all 80nt sequences that E. phage lambda does share with E.coli for k = 0
A roughly  4mb file called lamdaPhageW80k0_D with all 80nt sequences that E. phage lambda doesn't share with E.coli for k = 0
A file called lamdaPhageW80k0_Time.txt with the total runtime in milliseconds

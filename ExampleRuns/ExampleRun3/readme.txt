Folder A contains the genome for E.coli str. K12 substr. MG1655
Folder B contains the genome for Enterobacteria phage lambda

Run the following command (Make sure your machine has enough RAM: recommended amount is around 7Gig Ram ) : 

java -Xmx5000m -jar microTaboo.jar A B R 80 1 i 2 s

Expect: in the sub-folder R, you should now find:
A roughly 290kb file called EcoliK12MG1655W80k1_I with all 80nt sequences that E.coli does share with E. phage lambda for k = 1
A file called EcoliK12MG1655W80k1_Time.txt with the total runtime in milliseconds

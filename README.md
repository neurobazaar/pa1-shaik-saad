[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/T9GFMA2S)
[![Open in Visual Studio Code](https://classroom.github.com/assets/open-in-vscode-718a45dd9cf7e7f842a935f5ebbe5719a5e09af4491e668f4dbf3b35d5cca122.svg)](https://classroom.github.com/online_ide?assignment_repo_id=13628460&assignment_repo_type=AssignmentRepo)
## CSC435 Programming Assignment 1 (Winter 2024)
**Jarvis College of Computing and Digital Media - DePaul University**

**Student**: Shaik Saadullah Shareef (ssharee3@depaul.edu)

**Solution programming language**: Java

### Requirements

If you are implementing your solution in Java you will need to have Java 1.7.x and Maven 3.6.x installed on your systems. On Ubuntu 22.04 you can install Java and Maven using the following commands:

```
sudo apt install openjdk-17-jdk maven

```

### Setup

There are 5 datasets (Dataset1, Dataset2, Dataset3, Dataset4, Dataset5) that you need to use to evaluate your solution. Before you can evaluate your solution you need to download the datasets. You can download the datasets from the following link:

https://depauledu-my.sharepoint.com/:f:/g/personal/aorhean_depaul_edu/EgmxmSiWjpVMi8r6QHovyYIB-XWjqOmQwuINCd9N_Ppnug?e=TLBF4V

After you finished downloading the datasets copy them to the dataset directory (create the directory if it does not exist). Here is an example on how you can copy Dataset1 to the remote machine and how to unzip the dataset:

```
remote-computer$ cd <path-to-repo>
remote-computer$ mkdir datasets
local-computer$ scp Dataset1.zip cc@<remote-ip>:<path-to-repo>/datasets/.
remote-computer$ cd <path-to-repo>/datasets
remote-computer$ unzip Dataset1.zip
```

### Java solution
#### How to build/compile

To build the Java solution use the following commands:
```
cd app-java
mvn compile
mvn package
```

#### How to run application

To run the Java clean dataset program (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.CleanDataset <input directory> <output directory>
```

To run the Java word count program (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.CountWords <input directory> <output directory>
```

To run the Java sort_words program (after you build the project) use the following command:
```
java -cp target/app-java-1.0-SNAPSHOT.jar csc435.app.SortWords <input directory> <output directory>
```

#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>

int main() {

int pid, fd[2]; 
char buf [64];

if (pipe(fd) < 0) exit(1);

pid = fork();

if (pid == 0){ // child

    close(fd [0]); // close reader
    write(fd[1],"hello , world!",14);

}else { //parent
    close(fd [1]);  //close writer 
    if (read(fd[0],buf,64) > 0) 
    printf("Received: %s\n", buf); 
    waitpid(pid ,NULL,0);
}

}


#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>

int main() {
pid_t pid; 
pid = fork(); 
if(pid<0){ 
    perror("Fork-error");
    exit (1); }

if(pid==0) {
    printf("I am the child process\n");
    
    
}else{
    printf("I am the parent process\n");     
    execl("/bin/ls", "ls", "/home/eyu", NULL);
}

}



#include <sys/ipc.h> 
#include <sys/shm.h> 
#include <stdio.h> 
  
int main() 
{ 
    // ftok to generate unique key ftok(pathname) => same key generated for same pathname
    // 
    key_t key = ftok("shmfile",65); 
  
    // shmget returns an identifier in shmid 
    int shmid = shmget(key,1024,0666|IPC_CREAT); 
  
    // shmat to attach to shared memory 
    char *str = (char*) shmat(shmid,(void*)0,0); 
  
    gets(str); 
  
    printf("Data written in memory: %s\n",str); 
      
    //detach from shared memory  
    shmdt(str); 
  
    return 0; 
} 
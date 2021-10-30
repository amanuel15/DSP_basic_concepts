#include <sys/types.h>
#include <unistd.h>
#include <stdio.h>

int main()
{
    pid_t x;
    x = fork();
    printf("\n x=%d \n",x);
    printf("\nmy pid = %d \n", getpid());
    printf("\nmy parent pid = %d \n", getppid());

}

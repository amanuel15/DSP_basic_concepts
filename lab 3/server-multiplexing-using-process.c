#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <arpa/inet.h>
#include <fcntl.h> // for open
#include <unistd.h> // for close
#include <sys/types.h>

void socketThread(int clientSocket)
{

    char client_message[2000];
    char buffer[1024];
    int newSocket = clientSocket;
    recv(newSocket, client_message, 2000, 0);
    // Send message to the client socket
    char *message = malloc(sizeof(client_message) + 20);
    strcpy(message, "Hello Client : ");
    strcat(message, client_message);
    strcat(message, "\n");
    strcpy(buffer, message);
    free(message);
    sleep(1);
    send(newSocket, buffer, 13, 0);
    printf("Exit socketThread \n");
    close(newSocket);
    return NULL;
}

int main(){

    int serverSocket, newSocket;
    struct sockaddr_in serverAddr;
    struct sockaddr_storage serverStorage;
    socklen_t addr_size;
    pid_t pid[4];
    //Create the socket.
    serverSocket = socket(PF_INET, SOCK_STREAM, 0);
    // Configure settings of the server address struct
    // Address family = Internet
    serverAddr.sin_family = AF_INET;
    //Set port number, using htons function to use proper byte order
    serverAddr.sin_port = htons(7799);
    //Set IP address to localhost
    serverAddr.sin_addr.s_addr = htonl(INADDR_ANY);
    //Set all bits of the padding field to 0
    memset(serverAddr.sin_zero, '\0', sizeof serverAddr.sin_zero);
    //Bind the address struct to the socket
    bind(serverSocket, (struct sockaddr *)&serverAddr, sizeof(serverAddr));
    //Listen on the socket, with 40 max connection requests queued
    if (listen(serverSocket, 4) == 0)
        printf("Listening\n");
    else
        printf("Error\n");


    int i = 0;

    while (1)
    {
        /*---- Accept call creates a new socket for the incoming connection ----*/
        addr_size = sizeof serverStorage;
        newSocket = (serverSocket, (struct sockaddr *)&serverStorage, &addr_size);
        int pid_c = 0;
        if ((pid_c = fork()) == 0)
        {
            socketThread(newSocket);
        }
        else
        {
            pid[i++] = pid_c;
            if (i >= 3)
            {
                i = 0;
                while (i < 4)
                    waitpid(pid[i++], NULL, 0);
                i = 0;
            }
        }
    }
    return 0;
}
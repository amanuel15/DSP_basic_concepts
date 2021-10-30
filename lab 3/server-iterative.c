//https://www.tenouk.com/Module41b.html

#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
#define SERVER_PORT 12345

/* Run with a number of incoming connection as argument */

int main(int argc, char *argv[])
{
    int i, len, num, rc;
    int listen_sd, accept_sd;
    /* Buffer for data */
    char buffer[100];
    struct sockaddr_in addr;
    /* If an argument was specified, use it to */
    /* control the number of incoming connections */
    if (argc >= 2)
        num = atoi(argv[1]);
    /* Prompt some message */
    else
    {
        printf("Usage: %s <The_number_of_client_connection else 1 will be used>\n", argv[0]);
        num = 1;
    }

    /* Create an AF_INET stream socket to receive */
    /* incoming connections on */
    listen_sd = socket(AF_INET, SOCK_STREAM, 0);
    
    if (listen_sd < 0)
    {
        perror("Iserver - socket() error");
        exit(-1);
    }else
        printf("Iserver - socket() is OK\n");

    printf("Binding the socket...\n");

    /* Bind the socket */

    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = htonl(INADDR_ANY);
    addr.sin_port = htons(SERVER_PORT);
    rc = bind(listen_sd, (struct sockaddr *)&addr, sizeof(addr));

    if (rc < 0)
    {
        perror("Iserver - bind() error");
        close(listen_sd);
        exit(-1);
    }else
        printf("Iserver - bind() is OK\n");

    /* Set the listen backlog */

    rc = listen(listen_sd, 5);
    if (rc < 0){

        perror("Iserver - listen() error");
        close(listen_sd);
        exit(-1);
    }else
        printf("Iserver - listen() is OK\n");

    /* Inform the user that the server is ready */
    printf("The Iserver is ready!\n");
    /* Go through the loop once for each connection */
    
    for (i = 0; i < num; i++)
    {
        /* Wait for an incoming connection */
        printf("Iteration: #%d\n", i + 1);
        printf(" waiting on accept()\n");
        accept_sd = accept(listen_sd, NULL, NULL);
        if (accept_sd < 0)
        {
            perror("Iserver - accept() error");
            close(listen_sd);
            exit(-1);
        }else
            printf("accept() is OK and completed successfully!\n");

        /* Receive a message from the client */
        printf("I am waiting client(s) to send message(s) to me...\n");
        rc = recv(accept_sd, buffer, sizeof(buffer), 0);

        if (rc <= 0)
        {
            perror("Iserver - recv() error");
            close(listen_sd);
            close(accept_sd);
            exit(-1);
        }else
            printf("The message from client: \"%s\"\n", buffer);

        /* Echo the data back to the client */

        printf("Echoing it back to client...\n");
        len = rc;
        rc = send(accept_sd, buffer, len, 0);
        
        if (rc <= 0)
        {
            perror("Iserver - send() error");
            close(listen_sd);
            close(accept_sd);
            exit(-1);
        }else
            printf("Iserver - send() is OK.\n");

        /* Close the incoming connection */
        close(accept_sd);
    }

    /* Close the listen socket */
    close(listen_sd);
    return 0;
}
#include <stdio.h>
#include <stdlib.h>
#include <sys/socket.h>
#include <netinet/in.h>
/* Our server port as in the previous program */
#define SERVER_PORT 12345

main(int argc, char *argv[])
{
    int len, rc;
    int sockfd;
    char send_buf[100];
    char recv_buf[100];
    struct sockaddr_in addr;

    if (argc != 2)
    {
        printf("Usage: %s <Server_name or Server_IP_address>\n", argv[0]);
        exit(-1);
    }

    /* Create an AF_INET stream socket */

    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    if (sockfd < 0)
    {
        perror("client - socket() error");
        exit(-1);
    }else
        printf("client - socket() is OK.\n");

    /* Initialize the socket address structure */

    memset(&addr, 0, sizeof(addr));
    addr.sin_family = AF_INET;
    addr.sin_addr.s_addr = htonl(INADDR_ANY);
    addr.sin_port = htons(SERVER_PORT);

    /* Connect to the server */

    rc = connect(sockfd, (struct sockaddr *)&addr, sizeof(struct sockaddr_in));

    if (rc < 0)
    {
        perror("client - connect() error");
        close(sockfd);
        exit(-1);

    }else{
        printf("client - connect() is OK.\n");
        printf("connect() completed successfully.\n");
        printf("Connection with %s using port %d established!\n", argv[1], SERVER_PORT);
    }

    /* Enter data buffer that is to be sent */

    printf("Enter message to be sent to server:\n");
    gets(send_buf);

    /* Send data buffer to the worker job */

    len = send(sockfd, send_buf, strlen(send_buf) + 1, 0);

    if (len != strlen(send_buf) + 1)
    {
        perror("client - send() error");
        close(sockfd);
        exit(-1);

    }else
        printf("client - send() is OK.\n");

    printf("%d bytes sent.\n", len);

    /* Receive data buffer from the worker job */

    len = recv(sockfd, recv_buf, sizeof(recv_buf), 0);

    if (len != strlen(send_buf) + 1)
    {
        perror("client - recv() error");
        close(sockfd);
        exit(-1);

    }else{

        printf("client - recv() is OK.\n");
        printf("The sent message: \"%s\" successfully received by server and echoed back to client!\n", recv_buf);
        printf("%d bytes received.\n", len);
    }

    /* Close the socket */

    close(sockfd);
    return 0;
}
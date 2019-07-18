/*
 * ThreadManager will be constantly polling the status of jobQueue and the status of
 * ThreadPool every V seconds in order to decide whether to increase or decrease or maintain
 * the current number of the threads in the pool, according to the criteria: 
 * Nj<=T1(10)	T1(10)< Nj <= T2(20)	T2(20)<Nj<Capacity(50)
 * 
 * The ThreadManager terminates when a command “KILL” is sent through the client to the server.
 */

public class ThreadManager 
{

}

# nezumi-tspsolver
This is an incomplete traveling salesman problem(TSP) solver.
This can handle [TSPLIB](https://wwwproxy.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/tsp/) instances. 
This has two algorithms; Local Search (ls) and Simulated Annealing (sa).

## Usage

### Build
Use ant.
```shell
$ ant
```
Ant creates `tsp.jar` in `dest` directory.

### Download TSP instance
Access to [TSPLIB](https://wwwproxy.iwr.uni-heidelberg.de/groups/comopt/software/TSPLIB95/tsp/) and download some instances to instance directory. Then expand them.


### Solve
```shell
$ java -jar dest/tsp.jar -instance instance/att48.tsp -algorithm ls
```
This command solve att48.tsp instance using Local Search. If you would like to use Simmulated Annealing, change `ls` to `sa`. 

## LICENSE
 Apache License Version 2.0
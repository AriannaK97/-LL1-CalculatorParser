exp -> num TurnExp
	|  (exp)

TurnExp -> exp
		|  op exp
		|  ε

num -> digit TurnNum

TurnNum -> num
		| ε

op -> + | - | * | /

digit -> 0|1|2|3|4|5|6|7|8|9
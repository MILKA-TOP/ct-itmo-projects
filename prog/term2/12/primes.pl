prime(X) :- 
  primeDynamic(X),!.
  
prime(2) :-!.  

prime(X) :-
  X > 1,
  not(checkElementsNonPrime(X,2)),
  assert(primeDynamic(X)).
  
checkElementsNonPrime(X,Y) :-
  0 is X mod Y.

checkElementsNonPrime(X,Y) :-
  (Y * Y) =< X,
  Yplus is Y + 1,
  checkElementsNonPrime(X, Yplus).

composite(X) :- 
  compositeDynamic(X),!.

composite(X) :-
  not(prime(X)),
  assert(compositeDynamic(X)),!.

checkDivisors(X, I, Head) :- 
	0 is X mod I,
	prime(I), 
	Head = I,!.
	
checkDivisors(X, I, Head) :- 
  NEW_I is I + 1,
	checkDivisors(X, NEW_I, Head).



del(X, 0, X,X).
del([Head|X],1,X,Head).
del([Head|X], N, Y, Head) :- 
	N > 1, 
  Nmin is N - 1, 
  del(X, Nmin, Y, Head).

  
prime_divisors(1, []) :-!.

prime_divisors(X, [X]) :- 
	prime(X), !.
	
prime_divisors(X, [Head | Tail]) :- 
	power_divisors(X,1,[Head | Tail]),!.



power_divisors(1, _, []) :-!.

power_divisors(_, 0, []) :-!.
 
power_divisors(X, Pow, [Head | Tail]) :- 
	X > 1,
	checkDivisors(X, 2, Head),
	XPlus is div(X, Head), 
	del([Head | Tail], Pow, F,Head),
	power_divisors(XPlus, Pow, F),!.	

	

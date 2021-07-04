  
ins((Key, Value), nil, t(Key, Value, nil, nil)).

ins((Key, Value), t(TreeKey, TreeValue, LTree, RTree), t(OUTKey, OUTValue, OUTLTree, OUTRTree)) :-
	Key < TreeKey,
	ins((Key, Value), LTree, NewTree),
  OUTKey = TreeKey,
	OUTValue = TreeValue,
	OUTLTree = NewTree,
	OUTRTree = LLTree.

ins((Key, Value), t(TreeKey, TreeValue, LTree, RTree), t(OUTKey, OUTValue, OUTLTree, OUTRTree)) :-
	Key > TreeKey,
	ins((Key, Value), RTree, NewTree),
	OUTKey = TreeKey,
	OUTValue = TreeValue,
	OUTLTree = LTree,
	OUTRTree = NewTree.


building([], OutMap, OutMap):-!.

building([Head | Tail], NowMap, OutMap) :-
	ins(Head, NowMap, NewMap),
	building(Tail, NewMap, OutMap).


map_build(ListMap, TreeMap) :-
	building(ListMap, nil, TreeMap).

%Нашёл
map_get(t(Key, Value, _, _), Key, Value):-!.

%Искомый элемент слева
map_get(t(Key, Value, LeftTree, _), OriginalKey, OriginalValue) :- 
	OriginalKey < Key, 
	map_get(LeftTree, OriginalKey, OriginalValue).

%Искомый элемент справа
map_get(t(Key, Value, _, RightTree), OriginalKey, OriginalValue) :- 
	OriginalKey > Key, 
	map_get(RightTree, OriginalKey, OriginalValue).

map_keys(nil, []):-!.
map_keys(t(Key, _, nil, nil), [Key]):-!.
map_keys(t(Key, _, LeftTree, RightTree), Keys) :- 
	map_keys(LeftTree, LeftKeys),
	map_keys(RightTree, RightKeys),
	append(LeftKeys, [Key], LeftKeysK),
	append(LeftKeysK, RightKeys, Keys).


map_values(nil, []):-!.
map_values(t(_, Value, nil, nil), [Value]):-!.
map_values(t(_, Value, LeftTree, RightTree), Values) :- 
	map_values(LeftTree, LeftValues),
	map_values(RightTree, RightValues),
	append(LeftValues, [Value], LeftValuesV),
	append(LeftValuesV, RightValues, Values).
		
	
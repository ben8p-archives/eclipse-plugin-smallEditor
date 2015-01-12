package smalleditor.common.tokenizer;

public enum DocumentNodeType {
	OpenObject, OpenArray, CloseObject, CloseArray, Colon, Comma, False, True, Key,
	Number, String, Quote, Error, Null, OneLineComment, Todo, Fixme, NewLine, OpenMultilineComment, CloseMultilineComment, Function;
}

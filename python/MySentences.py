import os
import re
import nltk

class MySentences:
    def __init__(self, dirname):
        self.dirname = dirname

    def __iter__(self):
        for fname in os.listdir(self.dirname):
            for line in open(os.path.join(self.dirname, fname)):
				tokens = nltk.word_tokenize(line)
				yield tokens
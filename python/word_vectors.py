from gensim import models
import codecs
import json
import nltk
import os
import MySentences
from MySentences import MySentences
import numpy as np

model_file_name = 'word2vec.model'
dir_name = 'Corpus/English'
PRINT_STEP = 10
data_file = '../data/BioASQ-training4b/BioASQ-trainingDataset4b.json'

stop_words_file = 'stop_words.txt'
def get_stop_words(stop_words_file):
	stop_words = []
	with codecs.open(stop_words_file, encoding='utf-8') as f:
		while True:
			line = f.readline()
			print line
			if line == '':
				break
			stop_words.append(line.strip())
	return stop_words

def dump_sentences(file_name):
	with codecs.open(file_name, encoding = 'utf-8') as corpus_file:
	    data = json.load(corpus_file)
	x = data["questions"]
	# Dump
	with codecs.open('raw_data/raw_data.txt', 'a', encoding = 'utf-8') as f:
		for i in x:
			question = i['body']
			f.write(question + ' \n')
			para = i['ideal_answer'][0]
			sentences = nltk.tokenize.sent_tokenize(para)
			for s in sentences:
				if len(s) > 1:
					f.write(s + '\n')

def train_w2v_model(dir_name, model_file_name):
	sentences = MySentences(dir_name)
	w2v = models.Word2Vec(sentences, window = 5, min_count = 1, workers = 4)
	w2v.save(model_file_name)

def test_w2v_model(model_file_name):
	w2v = models.Word2Vec.load(model_file_name)
	print(w2v.similar_by_word('acid'))

	# print(w2v.most_similar(positive=['queen', 'war'], negative=['king'], topn=5))
	# print(w2v['station'])

if __name__ == '__main__':
	print(get_stop_words(stop_words_file))
	# dump_sentences(data_file)
	# train_w2v_model('raw_data', model_file_name)
	# test_w2v_model(model_file_name)
	# w2v = models.Word2Vec.load(model_file_name)
	# sent = nltk.word_tokenize('It is a big station.')
	# for token in sent:
	# 	if token in w2v:
	# 		vec = w2v[token]
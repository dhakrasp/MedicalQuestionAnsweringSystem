from gensim import models
import nltk
import os
import re
import tensorflow as tf
from tensorflow.models.rnn.translate import seq2seq_model
from MySentences import MySentences
import random
import numpy as np

model_file_name = 'word2vec.model'
dir_name = 'Corpus/English'
PRINT_STEP = 10
EPOCHS = 1000

def train_w2v_model(dir_name, model_file_name):
	sentences = MySentences(dir_name)
	w2v = models.Word2Vec(sentences, size = 100, window = 5, min_count = 1, workers = 4)
	w2v.save(model_file_name)

def test_w2v_model(model_file_name):
	w2v = models.Word2Vec.load(model_file_name)
	# print(w2v.similarity('is', 'fountain'))
	print(w2v.most_similar(positive=['queen', 'war'], negative=['king'], topn=5))
	# print(w2v['station'])

def train_seq2seq():
	embedding_size = 3
	data = []
	target = []
	xyvocab_size = 10
	yvocab_size = 10
	for i in xrange(yvocab_size):
		temp = []
		temp2 = []
		temp2.append(0)
		r = random.randint(1,10)
		temp.append(r)
		temp2.append(r)
		r = random.randint(1,10)
		temp.append(r)
		temp2.append(r)
		r = random.randint(1,10)
		temp.append(r)
		temp2.append(r)
		data.append(temp)
		target.append(temp2)
	data = np.array(data)
	target = np.multiply(target, target)
	# target = tf.convert_to_tensor(target)
	# data = tf.convert_to_tensor(data)
	# target = []
	# for i in xrange(10):
	# 	temp = []
	# 	temp.append(random.randint(1,2))
	# 	target.append(temp)
	# target = np.array(target)
	# print(data)
	# print(target)
	# return
	# input = tf.placeholder(tf.int32, [None,])
	input = tf.placeholder(shape=[None,], dtype=tf.int32)

	output = tf.placeholder(tf.int32, [None,])
	
	num_layers = 2

	basic_cell = tf.nn.rnn_cell.BasicLSTMCell(num_units=embedding_size)
	stacked_lstm = tf.nn.rnn_cell.MultiRNNCell([basic_cell]*num_layers, state_is_tuple=True)
	outputs, states = tf.nn.seq2seq.embedding_rnn_seq2seq(encoder_inputs=input, decoder_inputs=output, cell=stacked_lstm, num_encoder_symbols=embedding_size, num_decoder_symbols=embedding_size, embedding_size=embedding_size)

	loss_weights = [tf.ones_like(label, dtype=tf.float32) for label in  target]
	print(input.get_shape())
	print(output.get_shape())
	print(target.get_shape())
	# print(loss_weights)
	# return
	loss = tf.nn.seq2seq.sequence_loss(output, target, target)
	train_op = tf.train.AdamOptimizer(learning_rate=lr).minimize(loss)

	print('halting...')
	return
	correct_prediction = tf.equal(prediction, tf.cast(output, tf.int32))
	accuracy = tf.reduce_mean(tf.cast(correct_prediction, tf.float32))

	with tf.Session() as sess:
		sess.run(tf.global_variables_initializer())
		for i in range(EPOCHS):
			sess.run(train_op, feed_dict={input:data, output:target})
			if i % PRINT_STEP == 0:
				c = sess.run(cost, feed_dict={input:data, output:target})
				print('training cost:', c)
		print('Epoch {:2d} accuracy {:3.1f}%'.format(i + 1, 100 * accuracy.eval(feed_dict={input:data, output:target})))
		sess.close()

if __name__ == '__main__':
	# w2v = models.Word2Vec.load(model_file_name)
	# sent = nltk.word_tokenize('It is a big station.')
	# for token in sent:
	# 	if token in w2v:
	# 		vec = w2v[token]
	train_seq2seq()
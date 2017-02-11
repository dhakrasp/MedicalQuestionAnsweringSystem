import json
import nltk.tokenize
import pickle
import os
import codecs


def get_next_id(seed_file, init_seed=-1):
	if os.path.exists(seed_file):
		with open(seed_file, 'r') as f:
			seed = pickle.load(f)
	else:
		seed = init_seed

	seed = seed + 1

	with open(seed_file, 'w') as f:
		pickle.dump(seed, f)
	return seed

if __name__ == '__main__':
	with codecs.open('../data/BioASQ-training4b/BioASQ-trainingDataset4b.json', encoding = 'utf-8') as corpus_file:
	    data = json.load(corpus_file)
	x = data["questions"]

	# Dump ideal answers
	with codecs.open('data.txt', 'a', encoding = 'utf-8') as f:
		for i in x:
			para = i['ideal_answer'][0]
			sentences = nltk.tokenize.sent_tokenize(para)
			for s in sentences:
				line = u'{}{}{}'.format(get_next_id(seed_file='answers_seed'), '\t', s)
				f.write(line+'\n')
			# break

	# Dump questions
	with codecs.open('questions.txt', 'a', encoding = 'utf-8') as f:
		for i in x:
			question = i['body']
			line = u'{}{}{}'.format(get_next_id(seed_file='questions_seed'), '\t', question)
			f.write(line+'\n')

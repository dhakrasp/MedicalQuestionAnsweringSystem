import json
import pprint
import nltk.tokenize
import pickle
import os
import codecs

def get_next_id(init_seed = -1, seed_file='seed'):
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
	with codecs.open('../data/BioASQ-training4b/BioASQ-trainingDataset4b.json') as corpus_file:
	    data = json.load(corpus_file)
	x = data["questions"]
	init_seed = -1
	seed_file = 'seed'
	with codecs.open('data.txt', 'a') as f:
		for i in x:
			para = i['ideal_answer'][0]
			sentences = nltk.tokenize.sent_tokenize(para)
			for s in sentences:
				print(s)
				line = '{}{}{}'.format(get_next_id(), '\t', s)
				f.write(line+'\n')
			break
General Description:
This project is a ML/NLP library to analyze tweets and build predictive models. The build predictive models are built to help election/ad/marketing campaign to dig into social media conversations in order to get insights for making intelligent decisions.

The project consists of 4 main projects:
I) algorithms: this package contains implementation of all ml/nlp algorithms.
II) twitter: this package is designed to wrap twitter data regradless of the persistent layer that we use to store/retrieve tweets.
III) runanalysis: this package is an interface for runninf different ml/nlp algorithms.
IV) utilities: this package is developed to provide a collection of helper classes common across different analysis.
V) resources directory: This directory includes different data files which is used for tweets analysis like stop words, training data for sen
timent analysis and so on.

Packages Details:
I) algorithms package:
LDA class: an implentation for Latent Dirichlet Allocation algorithm which is used for topic modeling.
http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation
NaiveBayes class: a modified version of Naive Bayes classifier used for sentiment analysis.
http://en.wikipedia.org/wiki/Naive_Bayes_classifier
TextAnalysis class: a class for performing simple text analysis like computing word frequencies.
TweetsStatistics class: provides functionalities for computing basic statistics for tweets.

II) twitter package:
Tweet class: a representative class for tweets.
TweetDate: a class for dealing with date range. This allows us to analyze tweets in a give time range.
TweetsConstants: a class for constants and configuration data.
TwitterDataSource: an interface designed to deal with different persistent layers.
TwitterFileDataSource: an implementation of TwitterDataSource interface when persistent layer is File.
TwitterMySqlDataSource: an implementation of TwitterDataSource interface when persistent layer is MySql DB.

III) runanalysis:
RunBayes: runs sentiment analysis on tweets by using NaiveBayes class.
RunLDA: runs topic modeling on tweets using LDA class.
RunStatistics: runs basic statistics on tweets using TweetsStatistics class.
RunTextAnalysis: runs text analysis on tweets using TextAnalysis class.
ThreadPool & WorkerThread: a multi-threaded code for running analysis.

IV) utilities:
DayIntervals: a class which reads day interval files and returns a list of day pairs.
GenerateCsv: a class to generate CSV file for post-processing and visualization steps.
MapUtil: a class for printing a TreeMap data.
Pair: a class for defining pair objects.
SentimentLabel: sentiment labels.
StopWords: a class for building stop words for ML/NLP analysis.
TimeZone: time zone class.
TweetUtils: a helper class which has functionalities for cleaning and pre-process tweets.
ValueComparator: a comparator class.

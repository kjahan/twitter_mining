<h1>General Description:</h1>
This project is a ML/NLP library to analyze tweets and build predictive models. The predictive models are built to help election/ad/marketing campaigns to dig into social media conversations in order to get insights for making intelligent decisions.

The project consists of four main packages:
<ol>
<li><b>Algorithms</b> package contains implementation of all ml/nlp algorithms.</li>
<li><b>Twitter</b> package is designed to wrap twitter data regradless of the persistent layer that we use to store/retrieve tweets.</li>
<li>Runanalysis package is an interface for runninf different ml/nlp algorithms.</li>
<li>Utilities package is developed to provide a collection of helper classes common across different analysis.</li>
</ol>
 
Resources directory includes different data files which is used for tweets analysis like stop words, training data for sentiment analysis and so on.

Packages Details:
I) Algorithms Package:
LDA Algorithm: an implentation for Latent Dirichlet Allocation algorithm which is used for topic modeling.
http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation

NaiveBayes Classifier: a modified version of Naive Bayes classifier used for sentiment analysis.
http://en.wikipedia.org/wiki/Naive_Bayes_classifier

TextAnalysis: a class for performing simple text analysis like computing word frequencies.
TweetsStatistics class: provides functionalities for computing basic statistics for tweets.

II) Twitter Package:
Tweet: a representative class for tweets.
TweetDate: a class for dealing with date range. This allows us to analyze tweets in a give time range.
TweetsConstants: a class for constants and configuration data.
TwitterDataSource: an interface designed to deal with different persistent layers.
TwitterFileDataSource: an implementation of TwitterDataSource interface when persistent layer is File.
TwitterMySqlDataSource: an implementation of TwitterDataSource interface when persistent layer is MySql DB.

III) Runanalysis Package:
RunBayes: runs sentiment analysis on tweets by using NaiveBayes class.
RunLDA: runs topic modeling on tweets using LDA class.
RunStatistics: runs basic statistics on tweets using TweetsStatistics class.
RunTextAnalysis: runs text analysis on tweets using TextAnalysis class.
ThreadPool & WorkerThread: a multi-threaded code for running analysis.

IV) Utilities Package:
DayIntervals: a class which reads day interval files and returns a list of day pairs.
GenerateCsv: a class to generate CSV file for post-processing and visualization steps.
MapUtil: a class for printing a TreeMap data.
Pair: a class for defining pair objects.
SentimentLabel: sentiment labels.
StopWords: a class for building stop words for ML/NLP analysis.
TimeZone: time zone class.
TweetUtils: a helper class which has functionalities for cleaning and pre-process tweets.
ValueComparator: a comparator class.

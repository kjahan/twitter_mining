<h1>General Description:</h1>
This project is a ML/NLP library to analyze tweets and build predictive models. The predictive models are built to help election/ad/marketing campaigns to dig into social media conversations in order to get insights for making intelligent decisions.

The project consists of four main packages and a resource directory:
<ol>
<li><b>Algorithms</b> package contains implementation of all ml/nlp algorithms.</li>
<li><b>Twitter</b> package is designed to wrap twitter data regradless of the persistent layer that we use to store/retrieve tweets.</li>
<li><b>Runanalysis</b> package is an interface for runninf different ml/nlp algorithms.</li>
<li><b>Utilities</b> package is developed to provide a collection of helper classes common across different analysis.</li>
<li><b>Resources</b> directory includes different data files which is used for tweets analysis like stop words, training data for sentiment analysis and so on.</li>
</ol>

<h2>Packages Details:</h2>
<h3>Algorithms Package:</h3>
<ol>
<li><b>LDA Algorithm:<b> an implentation for Latent Dirichlet Allocation algorithm which is used for topic modeling.
<a href="http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation"> Latent Dirichlet Allocation algorithm</a></li>
<li><b>NaiveBayes Classifier:</b> a modified version of Naive Bayes classifier used for sentiment analysis.
<a href="http://en.wikipedia.org/wiki/Naive_Bayes_classifier">Naive Bayes classifier</a></li>
<li><b>TextAnalysis:</b> a class for performing simple text analysis like computing word frequencies.</li>
<li><b>TweetsStatistics:</b> provides functionalities for computing basic statistics for tweets.</li>
</ol>

<h3>Twitter Package:</h3>
<ol>
<li><b>Tweet:</b> a representative class for tweets.</li>
<li><b>TweetDate:</b> a class for dealing with date range. This allows us to analyze tweets in a give time range.</li>
<li><b>TweetsConstants:</b> a class for constants and configuration data.</li>
<li><b>TwitterDataSource:</b> an interface designed to deal with different persistent layers.</li>
<li><b>TwitterFileDataSource:</b> an implementation of TwitterDataSource interface when persistent layer is File.</li>
<li><b>TwitterMySqlDataSource:</b> an implementation of TwitterDataSource interface when persistent layer is MySql DB.</li>
</ol>

<h3>Runanalysis Package:</h3>
<ol>
<li><b>RunBayes:</b> runs sentiment analysis on tweets by using NaiveBayes class.</li>
<li><b>RunLDA:</b> runs topic modeling on tweets using LDA class.</li>
<li><b>RunStatistics:</b> runs basic statistics on tweets using TweetsStatistics class.</li>
<li><b>RunTextAnalysis:</b> runs text analysis on tweets using TextAnalysis class.</li>
<li><b>ThreadPool & WorkerThread:</b> a multi-threaded code for running analysis.</li>
</ol>

<h3>Utilities Package:</h3>
<ol>
<li><b>DayIntervals:</b> a class which reads day interval files and returns a list of day pairs.</li>
<li><b>GenerateCsv:</b> a class to generate CSV file for post-processing and visualization steps.</li>
<li><b>MapUtil:</b> a class for printing a TreeMap data.</li>
<li><b>Pair:</b> a class for defining pair objects.</li>
<li><b>SentimentLabel:</b> sentiment labels.</li>
<li><b>StopWords:</b> a class for building stop words for ML/NLP analysis.</li>
<li><b>TimeZone:</b> time zone class.</li>
<li><b>TweetUtils:</b> a helper class which has functionalities for cleaning and pre-process tweets.</li>
<li><b>ValueComparator:</b> a comparator class.</li>
</ol>

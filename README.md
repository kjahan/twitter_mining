Twitter Mining Project
======================

<h1>General Description:</h1>
This project is a ML/NLP library in Java for analyzing tweets and building predictive models. The predictive models are built to help election/ad/marketing campaigns dig into social media conversations (public opinions) in order to get insights for making intelligent decisions.

The project consists of four main packages and a resource directory:
<ol>
<li><b>Algorithms</b> package contains implementations of a few ml/nlp algorithms for running text analysis on tweets contents.</li>
<li><b>Twitter</b> package is designed to wrap twitter data regradless of the persistent layer that one uses to store/retrieve tweets.</li>
<li><b>Runanalysis</b> package is the interface for running ml/nlp algorithms.</li>
<li><b>Utilities</b> package is developed to provide a collection of helper classes for different analysis.</li>
<li><b>Resources</b> directory includes a few data sources used for tweets analysis such as stop words, training data for sentiment analysis and so on.</li>
</ol>

<h2>Packages Details:</h2>
<h3>Algorithms Package:</h3>
<ol>
<li><b>LDA Algorithm:</b> an implentation of <a href="http://en.wikipedia.org/wiki/Latent_Dirichlet_allocation"> Latent Dirichlet Allocation algorithm</a> used for topic modeling.</li>
<li><b>NaiveBayes Classifier:</b> a customized version of <a href="http://en.wikipedia.org/wiki/Naive_Bayes_classifier">Naive Bayes classifier</a> for running sentiment analysis on tweets.</li>
<li><b>TextAnalysis:</b> a class for performing various text analysis such as computing word frequencies.</li>
<li><b>TweetsStatistics:</b> provides functionalities for computing basic statistics from tweets.</li>
</ol>

<h3>Twitter Package:</h3>
<ol>
<li><b>Tweet:</b> a representative class for tweets.</li>
<li><b>TweetDate:</b> a class for dealing with date range. This allows us to analyze tweets in a give time range.</li>
<li><b>TweetsConstants:</b> a class for constants and configuration parameters.</li>
<li><b>TwitterDataSource:</b> an interface designed to deal with different persistent layers.</li>
<li><b>TwitterFileDataSource:</b> an implementation of TwitterDataSource interface when persistent layer is raw File.</li>
<li><b>TwitterMySqlDataSource:</b> an implementation of TwitterDataSource interface when persistent layer is MySql DB.</li>
</ol>

<h3>Runanalysis Package:</h3>
<ol>
<li><b>RunBayes:</b> runs sentiment analysis on tweets using NaiveBayes class.</li>
<li><b>RunLDA:</b> runs topic modeling on tweets using LDA class.</li>
<li><b>RunStatistics:</b> runs basic statistics on tweets using TweetsStatistics class.</li>
<li><b>RunTextAnalysis:</b> runs text analysis on tweets using TextAnalysis class.</li>
<li><b>ThreadPool & WorkerThread:</b> a multi-threaded code for running analysis.</li>
</ol>

<h3>Utilities Package:</h3>
<ol>
<li><b>DayIntervals:</b> a class for reading day interval files and generating a list of day pairs.</li>
<li><b>GenerateCsv:</b> a class for generating a CSV file for post-processing and visualization steps.</li>
<li><b>MapUtil:</b> a class for printing a TreeMap data.</li>
<li><b>Pair:</b> a class for defining pair objects.</li>
<li><b>SentimentLabel:</b> sentiment labels.</li>
<li><b>StopWords:</b> a class for building stop words for NLP analysis.</li>
<li><b>TimeZone:</b> time zone class.</li>
<li><b>TweetUtils:</b> a helper class which has functionalities for cleaning/normalizing tweets.</li>
<li><b>ValueComparator:</b> a comparator class.</li>
</ol>
<p>
<h3>Tweets Data Schema:</h3>
This library requires your twitter data to be stored in a MySql database/table (i.e. politics/tweets). Schema of tweets table is shown below:
<table style="width:300px">
<tr>
  <td><b>Field</b></td>
  <td><b>Type</b></td>
</tr>
<tr>
  <td>id</td>
  <td>int(10) unsigned, PRI</td> 
</tr>
<tr>
  <td>timestamp</td>
  <td>int(10) unsigned</td>
</tr>
<tr>
  <td>source</td>
  <td>varchar(40)</td>
</tr>
<tr>
  <td>author</td>
  <td>varchar(20)</td>
</tr>
<tr>
  <td>lat</td>
  <td>decimal(10,8)</td>
</tr>
<tr>
  <td>lng</td>
  <td>decimal(11,8)</td>
</tr>
<tr>
  <td>text</td>
  <td>varchar(140)</td>
</tr>
<tr>
  <td>created_at</td>
  <td>datetime</td>
</tr>
</table>
</p>
<p>

## Licence

    Copyright (c) 2013 Black Square Media Ltd. All rights reserved.
    (The MIT License)

    Permission is hereby granted, free of charge, to any person obtaining
    a copy of this software and associated documentation files (the
    'Software'), to deal in the Software without restriction, including
    without limitation the rights to use, copy, modify, merge, publish,
    distribute, sublicense, and/or sell copies of the Software, and to
    permit persons to whom the Software is furnished to do so, subject to
    the following conditions:

    The above copyright notice and this permission notice shall be
    included in all copies or substantial portions of the Software.

    THE SOFTWARE IS PROVIDED 'AS IS', WITHOUT WARRANTY OF ANY KIND,
    EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
    MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
    IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
    CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
    TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
    SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

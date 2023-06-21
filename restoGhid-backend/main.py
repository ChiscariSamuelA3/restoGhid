import pandas as pd
from sklearn.feature_extraction.text import TfidfVectorizer
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA
from sklearn.metrics import silhouette_score

df = pd.read_csv('restaurants.csv')

features = df[['location_id', 'subcategory_key', 'cuisines', 'rating']]
features = features.fillna('')
features['cuisines'] = features['cuisines'].apply(lambda x: x.replace(',', ' '))
features['subcategory_key'] = features['subcategory_key'].apply(lambda x: x.replace(',', ' '))

features['text'] = features['subcategory_key'] + ' ' + features['cuisines'] + ' ' + features['rating'].astype(str)

# Vectorize the text
vectorizer = TfidfVectorizer(stop_words='english')
X = vectorizer.fit_transform(features['text'])

# Dimensionality reduction with PCA
pca = PCA(n_components=2)
X_pca = pca.fit_transform(X.toarray())

# Try different values for the number of clusters and calculate the Silhouette score
scores = []
for k in range(2, 10):
    kmeans = KMeans(n_clusters=k, random_state=0, n_init=10, init='k-means++').fit(X)
    labels = kmeans.labels_
    score = silhouette_score(X, labels, metric='euclidean')
    scores.append(score)

best_k = scores.index(max(scores)) + 2

# Clustering with the optimal number of clusters
kmeans = KMeans(n_clusters=best_k, random_state=0, n_init=10, init='k-means++')
kmeans.fit(X)

features['cluster'] = kmeans.labels_

clusters = features.groupby('cluster')
df_clusters = []
for cluster in clusters.groups:
    df = clusters.get_group(cluster)[['location_id', 'subcategory_key', 'cuisines', 'rating', 'cluster']]
    # sort by rating
    df = df.sort_values(by=['rating'], ascending=False)
    df_clusters.append(df)

# display results in a single file
df_clusters = pd.concat(df_clusters)
df_clusters.to_csv('clusters_all.csv', index=False)

# print the number of restaurants in each cluster
print(features.groupby('cluster').count()['location_id'])

# print the best silhouette score
print('Best Silhouette Score:', max(scores))

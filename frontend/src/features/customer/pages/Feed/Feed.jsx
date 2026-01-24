import { Layout } from "../../components/layout/Layout/Layout";
import { ArticleCard } from "../../components/ui/ArticleCard/ArticleCard";
import classes from "./Feed.module.css";
import { useState, useEffect } from "react";

export const Feed = () => {
  const [articles, setArticles] = useState([]);
  const [loadingArticles, setLoadingArticles] = useState(true);

  useEffect(() => {
    fetchArticles();
  }, []);

  const fetchArticles = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(
        "http://127.0.0.1:8080/admin/articles",
        { headers: { Authorization: `Bearer ${token}` } }
      );
      if (!response.ok) throw new Error("Failed to fetch articles");
      const data = await response.json();
      setArticles(data);
    } catch (error) {
      console.error("Error fetching articles:", error);
    } finally {
      setLoadingArticles(false);
    }
  };

  return (
    <Layout>
      <div className={classes.FeedContainer}>
        {loadingArticles ? (
          <p>Loading articles...</p>
        ) : (
          articles.map((article) => (
            <ArticleCard
              key={article.id}
              articleId={article.id}
              articleTitle={article.title}
              articleDesc={article.description}
              articleImg={article.imageUrl}
              articleDate={new Date(article.createdAt).toLocaleDateString("pl-PL")}
            />
          ))
        )}
      </div>
    </Layout>
  );
};

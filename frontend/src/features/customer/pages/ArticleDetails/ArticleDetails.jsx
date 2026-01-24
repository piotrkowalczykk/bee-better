import { useState, useEffect } from "react";
import { useParams } from "react-router-dom";
import { Layout } from "../../components/layout/Layout/Layout";
import classes from "./ArticleDetails.module.css";

export const ArticleDetails = () => {
  const { articleId } = useParams();
  const [article, setArticle] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchArticle();
  }, [articleId]);

  const fetchArticle = async () => {
    const token = localStorage.getItem("token");
    try {
      const response = await fetch(`http://localhost:8080/customer/articles/${articleId}`, {
        headers: {
          Authorization: `Bearer ${token}`,
        },
      });
      if (!response.ok) throw new Error("Failed to fetch article");
      const data = await response.json();
      setArticle(data);
    } catch (error) {
      console.error("Error fetching article:", error);
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <Layout><p>Loading...</p></Layout>;
  if (!article) return <Layout><p>Article not found</p></Layout>;

  return (
    <Layout>
      <div className={classes.articleDetailsContainer}>
        <div className={classes.articleDetailsImgContainer}>
          <img src={"http://localhost:8080" + article.imageUrl} alt={article.title} />
        </div>
        <div className={classes.articleDetailsContent}>
          <h2 className={classes.articleTitle}>{article.title}</h2>
          <br />
          <p className={classes.articleDesc}>{article.description}</p>
          <br />
          <p className={classes.articleContent}>{article.content}</p>
          <br />
          <p className={classes.articleDate}>
            {new Date(article.createdAt).toLocaleDateString("pl-PL")}
          </p>
        </div>
      </div>
    </Layout>
  );
};

import classes from "./ArticleCard.module.css";
import { Link } from "react-router-dom";

export const ArticleCard = ({
  articleId,
  articleTitle,
  articleDesc,
  articleImg,
  articleDate,
}) => {
  return (
    <div className={classes.articleCardContainer}>
      <div className={classes.articlContent}>
        <h2 className={classes.articleTitle}>{articleTitle}</h2>
        <p className={classes.articleDesc}>{articleDesc}</p>
        <div className={classes.articleContentFooter}>
          <Link to={`/articles/${articleId}`} className={classes.articleBtn}>
            Read more...
          </Link>
          <label className={classes.articleDate}>{articleDate}</label>
        </div>
      </div>
      <div className={classes.articleImageContainer}>
        <img src={"http://localhost:8080" + articleImg} />
      </div>
    </div>
  );
};

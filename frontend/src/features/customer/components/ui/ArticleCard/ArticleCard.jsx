import classes from "./ArticleCard.module.css"

export const ArticleCard = () => {
    return (
        <div className={classes.articleCardContainer}>
            <div className={classes.articlContent}>
                <h2 className={classes.articleTitle}>Happy New Year!</h2>
                <p className={classes.articleDesc}>Lorem ipsum dolor sit amet, commodo vitae aliquainte euismod eros. Donec justo diam, mollis sit amet luctus in, auctor ut neque. Praesent pellentesque commodo sem, ut lacinia odio rutrum ut. In vehicula nunc ac nisi tincidunt, sit amet vulputate libero rhoncus. Integer lacinia vel libero id pellentesque. Donec iaculis vehicula odio, eu imperdiet tortor.</p>
                <div className={classes.articleContentFooter}>
                    <button>Read more...</button>
                    <label className={classes.articleDate}>1.01.2026</label>
                </div>
            </div>
            <div className={classes.articleImageContainer}>
                <img src="https://c8.alamy.com/comp/3BM1F7Y/2026-fireworks-poster-gold-number-new-year-greeting-card-3BM1F7Y.jpg" />
            </div>
        </div>
    );
}
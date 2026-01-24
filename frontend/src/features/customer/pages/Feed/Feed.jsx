import { Layout } from "../../components/layout/Layout/Layout";
import { IconPicker } from "../../../../components/ui/IconPicker/IconPicker";
import { ArticleCard } from "../../components/ui/ArticleCard/ArticleCard";
import classes from "./Feed.module.css"

export const Feed = () => {
    return (
        <Layout>
            <div className={classes.FeedContainer}>
                <ArticleCard />
                <ArticleCard />
                <ArticleCard />
            </div>
        </Layout>
    );
}
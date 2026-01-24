import { useEffect, useState } from "react";
import { CustomBtn } from "../../../../components/ui/CustomBtn/CustomBtn";
import { ImageUploader } from "../../../../components/ui/ImageUploader/ImageUploader";
import { Layout } from "../../components/layout/Layout/Layout";
import { CustomInput } from "../../components/ui/CustomInput/CustomInput";
import classes from "./Admin.module.css";
import { DeleteIcon, EditIcon, EyeIcon } from "../../../../app/icons/Icons";
import { useNavigate } from "react-router-dom";

export const Admin = () => {
  const [articles, setArticles] = useState([]);
  const [loadingArticles, setLoadingArticles] = useState(true);

  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [content, setContent] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const [imagePreviewUrl, setImagePreviewUrl] = useState(null);
  const [editingArticleId, setEditingArticleId] = useState(null);

  const navigate = useNavigate();

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

  const handleDelete = async (article) => {
    const token = localStorage.getItem("token");
    if (!window.confirm("Are you sure you want to delete this article?"))
      return;

    try {
      const response = await fetch(
        `http://localhost:8080/admin/articles/${article.id}`,
        {
          method: "DELETE",
          headers: { Authorization: `Bearer ${token}` },
        }
      );
      if (!response.ok) throw new Error("Failed to delete article");
      setArticles(articles.filter((a) => a.id !== article.id));
    } catch (error) {
      console.error("Delete error:", error);
    }
  };

  const handleEdit = (article) => {
    setEditingArticleId(article.id);
    setTitle(article.title);
    setDescription(article.description);
    setContent(article.content);
    setImageFile(null);
    setImagePreviewUrl("http://localhost:8080" + article.imageUrl); 
  };

  const handleView = (id) => {
    navigate(`/articles/${id}`);
  };

  const handleImageSelect = (file) => {
    setImageFile(file);
  };

  const handleSave = async () => {
    const token = localStorage.getItem("token");
    if (!title || !description || !content) {
      alert("Fill all fields");
      return;
    }

    try {
      const formData = new FormData();
      formData.append("title", title);
      formData.append("description", description);
      formData.append("content", content);
      if (imageFile) formData.append("image", imageFile);

      const method = editingArticleId ? "PUT" : "POST";
      const url = editingArticleId
        ? `http://localhost:8080/admin/articles/${editingArticleId}`
        : `http://localhost:8080/admin/articles`;

      const response = await fetch(url, {
        method,
        headers: {
          Authorization: `Bearer ${token}`,
        },
        body: formData,
      });

      if (!response.ok) throw new Error("Failed to save article");
      const savedArticle = await response.json();

      if (editingArticleId) {
        setArticles(
          articles.map((a) => (a.id === savedArticle.id ? savedArticle : a))
        );
      } else {
        setArticles([savedArticle, ...articles]);
      }

      setTitle("");
      setDescription("");
      setContent("");
      setImageFile(null);
      setEditingArticleId(null);
      setImagePreviewUrl(null);
    } catch (error) {
      console.error("Save error:", error);
    }
  };

  const formatDate = (dateString) => {
    if (!dateString) return;
    const date = new Date(dateString);
    return date.toLocaleString("pl-PL", {
      day: "2-digit",
      month: "2-digit",
      year: "numeric",
      hour: "2-digit",
      minute: "2-digit",
    });
  };

  return (
    <Layout>
      <div className={classes.adminContainer}>
        <div className={classes.adminLeftContainer}>
          <h2 className={classes.containerTitle}>Manage article</h2>
          <div className={classes.innerAdminLeftContainer}>
            <div className={classes.titleAndImageContainer}>
              <CustomInput
                label="Title"
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
              />
              <ImageUploader image={imageFile} onFileSelect={handleImageSelect}  preview={imagePreviewUrl}/>
            </div>
            <div className={classes.descriptionContainer}>
              <CustomInput
                label="Description"
                type="textarea"
                rows="7"
                value={description}
                onChange={(e) => setDescription(e.target.value)}
              />
            </div>
            <div className={classes.contentContainer}>
              <CustomInput
                label="Content"
                type="textarea"
                rows="7"
                value={content}
                onChange={(e) => setContent(e.target.value)}
              />
            </div>
            <div className={classes.manageBtns}>
              <CustomBtn
                text="Clear"
                bgColor="rgb(130, 76, 255)"
                color="white"
                onClick={() => {
                  setTitle("");
                  setDescription("");
                  setContent("");
                  setImageFile(null);
                  setEditingArticleId(null);
                  setImagePreviewUrl(null);
                }}
              />
              <CustomBtn
                text="Save"
                bgColor="#209d3dff"
                color="white"
                onClick={handleSave}
              />
            </div>
          </div>
        </div>

        <div className={classes.adminRightContainer}>
          <h2 className={classes.containerTitle}>Articles</h2>
          <div className={classes.innerAdminRightContainer}>
            {loadingArticles ? (
              <p>Loading articles...</p>
            ) : (
              <table className={classes.articleTable}>
                <thead>
                  <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Created at</th>
                    <th>Modified at</th>
                    <th>Manage</th>
                  </tr>
                </thead>
                <tbody>
                  {articles.map((article) => (
                    <tr key={article.id}>
                      <td>{article.id}</td>
                      <td>{article.title}</td>
                      <td>{formatDate(article.createdAt)}</td>
                      <td>{formatDate(article.modifiedAt)}</td>
                      <td>
                        <button
                          className={classes.articleBtn}
                          style={{ backgroundColor: "yellow" }}
                          onClick={() => handleEdit(article)}
                        >
                          <EditIcon />
                        </button>
                        <button
                          className={classes.articleBtn}
                          style={{ backgroundColor: "rgb(255, 0, 0)" }}
                          onClick={() => handleDelete(article)}
                        >
                          <DeleteIcon />
                        </button>
                        <button
                          className={classes.articleBtn}
                          style={{ backgroundColor: "rgb(154, 2, 255)" }}
                          onClick={() => handleView(article.id)}
                        >
                          <EyeIcon />
                        </button>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            )}
          </div>
        </div>
      </div>
    </Layout>
  );
};

import React from "react";
import { useSelector } from "react-redux";
import { selectAccessToken } from "../../Redux/data/AuthSlice";
import { Container } from "react-bootstrap";
import { getAllTicket, userUseVoucher } from "../../services/TicketServices";
import { toast } from "react-toastify";
import Paginate from "../Util/Paginate";

const Ticket = (props) => {
  const { id } = props;
  const accessToken = useSelector(selectAccessToken);
  const [listTicket, setListTicket] = React.useState([]);
  const [currentPage, setCurrentPage] = React.useState(1);
  const [limit, setLimit] = React.useState(3);
  const [totalItem, setTotalItem] = React.useState(0);
  const [totalPage, setTotalPage] = React.useState(0);
  const [openTicket, setOpenTicket] = React.useState(false);

  React.useEffect(() => {
    getTicket(id, currentPage, limit);
  }, []);

  const getTicket = async (id, page, limit) => {
    await getAllTicket(id, page, limit)
      .then((rs) => {
        if (rs) {
          setCurrentPage(rs.page);
          setTotalItem(rs.totalItem);
          setTotalPage(rs.totalPage);
          setListTicket(rs.data);
        }
      })
      .catch((err) => console.log(err.message));
  };

  const handleUseVoucher = async (serialCode) => {
    await userUseVoucher(serialCode)
      .then((rs) => {
        if (rs) {
          toast.success("Use voucher is successfully !");
          setOpenTicket(true);
        }
      })
      .catch((err) => console.log(err.message));
  };

  const handlePageClick = (event) => {
    getTicket(id, +event.selected + 1, limit);
  };

  return (
    <>
      {accessToken && (
        <Container>
          {listTicket
            ? listTicket.map((item, key) => {
                return (
                  <div key={key} className="d-flex p-3">
                    <div className="pe-3">
                      <img
                        alt=""
                        src={item.bannerUrl}
                        style={{
                          width: 20 + "rem",
                          height: 10 + "rem",
                          borderRadius: 10,
                        }}
                      />
                    </div>
                    <div>
                      <div>
                        <h4>{item.warehouseName}</h4>
                        <p>{item.categoryName}</p>
                      </div>
                      <div>
                        <button
                          className="btn btn-primary"
                          onClick={() => handleUseVoucher(item.serialCode)}
                        >
                          <i class="fa-solid fa-copy"></i>
                        </button>
                        {openTicket && (
                          <>
                            <input value={item.serialCode} />
                          </>
                        )}
                      </div>
                    </div>
                  </div>
                );
              })
            : "No data"}
          <Paginate totalPages={totalPage} handlePageClick={handlePageClick} />
        </Container>
      )}
    </>
  );
};

export default Ticket;

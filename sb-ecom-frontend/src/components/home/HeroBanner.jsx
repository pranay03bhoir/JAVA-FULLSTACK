import React from "react";
import { Swiper, SwiperSlide } from "swiper/react";

// Import Swiper styles
import "swiper/css";
import "swiper/css/navigation";
import "swiper/css/pagination";
import "swiper/css/scrollbar";
import "swiper/css/effect-fade";
import "swiper/css/autoplay";
import { Autoplay, Pagination, EffectFade, Navigation } from "swiper/modules";
import { bannerList } from "../../../utils/index.js";
import { Link } from "react-router-dom";
const HeroBanner = () => {
  const colors = [
    `bg-banner-color1`,
    `bg-banner-color2`,
    `bg-banner-color3`,
    `bg-banner-color4`,
    `bg-banner-color5`,
    `bg-banner-color6`,
    `bg-banner-color7`,
    `bg-banner-color8`,
    `bg-banner-color9`,
    `bg-banner-color10`,
  ];
  return (
    <div className={` rounded-md banner`}>
      <Swiper
        grabCursor={true}
        autoplay={{
          delay: 4000,
          disableOnInteraction: false,
        }}
        navigation
        modules={[Pagination, EffectFade, Navigation, Autoplay]}
        pagination={{ clickable: true }}
        scrollbar={{ draggable: true }}
        slidesPerView={1}
      >
        {bannerList.map((item, i) => (
          <SwiperSlide key={item.id}>
            <div className={`carousel-item rounded-md  md:h-96 ${colors[i]}`}>
              <div className={`flex items-center justify-center`}>
                <div className={`hidden lg:flex justify-center w-1/2 p-8`}>
                  <div className={`text-center`}>
                    <h3 className={`text-3xl text-white font-bold`}>
                      {item.title}
                    </h3>
                    <h1 className={`text-5xl text-white font-bold mt-2`}>
                      {item.subtitle}
                    </h1>
                    <p className={`text-white font-bold mt-4`}>
                      {item.description}
                    </p>
                    <Link
                      to={`/products`}
                      className={`mt-6 inline-block bg-black text-white py-2 px-4 rounded hover:bg-gray-800`}
                    >
                      Shop
                    </Link>
                  </div>
                </div>
                <div className={`w-full flex justify-center lg:w-1/2 p-4`}>
                  <img
                    src={item.image}
                    alt={item.title}
                    className={`rounded-md`}
                  />
                </div>
              </div>
            </div>
          </SwiperSlide>
        ))}
      </Swiper>
    </div>
  );
};

export default HeroBanner;

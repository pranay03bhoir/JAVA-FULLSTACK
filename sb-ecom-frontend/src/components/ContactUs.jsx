import React from "react";
import { FaEnvelope, FaPhone } from "react-icons/fa6";
import { FaMapMarkedAlt } from "react-icons/fa";

const ContactUs = () => {
  return (
    <div
      className={`flex flex-col items-center justify-center min-h-screen py-12 bg-cover bg-center`}
      style={{
        backgroundImage:
          "url('https://images.pexels.com/photos/1266808/pexels-photo-1266808.jpeg')",
      }}
    >
      <div className={`bg-white shadow-lg rounded-lg p-8 w-full max-w-lg`}>
        <h1 className={`text-4xl font-bold text-center mb-6`}>Contact Us</h1>
        <p className="text-center mb-6 text-gray-600">
          If you have any inquiries or need assistance, we appreciate your
          interest in contacting us. Please feel free to use the provided form
          or reach out to us directly using the contact details provided below.
          We are committed to providing prompt and helpful responses.
        </p>
        <form className="space-y-4">
          <div className="flex flex-col space-y-2">
            <label className="block text-sm font-medium text-gray-700">
              Name
            </label>
            <input
              type="text"
              required
              className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            />
          </div>
          <div className="flex flex-col space-y-2">
            <label className="block text-sm font-medium text-gray-700">
              Email
            </label>
            <input
              type="email"
              required
              className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            />
          </div>
          <div className="flex flex-col space-y-2">
            <label className="block text-sm font-medium text-gray-700">
              Message
            </label>
            <textarea
              rows={4}
              required
              className="px-3 py-2 border border-gray-300 rounded-md shadow-sm focus:outline-none focus:ring-blue-500 focus:border-blue-500 sm:text-sm"
            />
          </div>
          <button
            type="submit"
            className=" w-full px-4 py-2 bg-blue-500 text-white rounded-md hover:bg-blue-600 focus:outline-none focus:ring-blue-600"
          >
            Send Message
          </button>
        </form>
        <div className={`mt-8 flex flex-col items-center space-y-4`}>
          <h2 className={`text-lg font-semibold`}>Contact Information:</h2>
          <div className={`flex items-center space-x-4`}>
            <FaPhone className={`text-blue-500`} />
            <span className={`text-gray-600`}>+91 8900776565</span>
          </div>
          <div className={`flex items-center space-x-4`}>
            <FaEnvelope className={`text-blue-500`} />
            <span className={`text-gray-600`}>
              testOfficialImage999@gmail.com
            </span>
          </div>
          <div className={`flex items-center space-x-4`}>
            <FaMapMarkedAlt className={`text-blue-500`} />
            <span className={`text-gray-600`}>
              123 temple road, BKC, Mumbai 400001
            </span>
          </div>
        </div>
      </div>
    </div>
  );
};

export default ContactUs;
